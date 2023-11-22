package dbclient

import (
	"context"
	"fmt"
	"time"
	pb "zpi/pb"
	queries "zpi/sql/gen"

	"github.com/jackc/pgx/v4"
	"go.uber.org/zap"
)

// CreateAssessorRatings creates a new AssessorRatings object with the given rating ID, assessor ID, first name, and last name.
// It retrieves partial ratings for the given assessor and rating ID, and remaps the values to create the new AssessorRatings object.
// Returns the new AssessorRatings object and an error if there was any issue retrieving the partial ratings.
func (st *Store) CreateAssessorRatings(ctx context.Context, ratingId int32, assessorId int32, firstN, lastN string, isDraft bool) (*pb.AssessorRatings, error) {

	// Get partial ratings for this assessor and rating id
	r, err := queries.New(st.Pool).GetPRatingsForAssessorAndRatingID(ctx,
		queries.GetPRatingsForAssessorAndRatingIDParams{RatingID: ratingId, AssessorID: assessorId},
	)
	if err != nil && err != pgx.ErrNoRows {
		return nil, fmt.Errorf("getting partial ratings for assessor %d and rating id %d: %w", assessorId, ratingId, err)
	}

	// Remap values
	initialRs := &pb.AssessorRatings{
		RatingId:       ratingId,
		AssessorId:     assessorId,
		FirstName:      firstN,
		LastName:       lastN,
		IsDraft:        isDraft,
		PartialRatings: []*pb.PartialRating{},
	}
	for _, rt := range r {
		initialRs.PartialRatings = append(initialRs.PartialRatings, &pb.PartialRating{
			PartialRatingId: rt.PartialRatingID,
			CriterionId:     rt.CriterionID,
			Points:          rt.Points,
			Justification:   rt.Justification,
			Modified:        rt.Modified.Format(time.RFC3339),
			ModifiedBy:      rt.ModifiedByID,
		})
	}

	return initialRs, nil
}

// TODO: explain this method
func (st *Store) CreateNewSubmissionRating(ctx context.Context, assessorId int32, submissionId int32, rtype pb.RatingType) (*pb.Rating, error) {
	hasAccess, err := queries.New(st.Pool).DoesAssessorHaveAccess(ctx, AccessParams{AssessorID: assessorId, SubmissionID: submissionId})
	if err != nil {
		return nil, fmt.Errorf("checking access: %w", err)
	}
	if !hasAccess {
		return nil, fmt.Errorf("User does not have access to resource")
	}

	// For individual inserts we use provided Assessor id
	if rtype.Type() == pb.RatingType_INDIVIDUAL.Type() {
		r, err := queries.New(st.Pool).CreateProjectRatingIndividual(ctx, queries.CreateProjectRatingIndividualParams{
			AssessorID:   assessorId,
			SubmissionID: submissionId,
			IsDraft:      true,
			Type:         RatingTypesMappingRev[rtype],
		})

		if err != nil {
			return nil, fmt.Errorf("creating new submission rating: %w", err)
		}

		return &pb.Rating{
			RatingId:   r.RatingID,
			AssessorId: r.AssessorID,
			IsDraft:    r.IsDraft,
			Type:       rtype,
		}, nil
	}

	// ...else we have to find lead assessor id and use it
	r, err := queries.New(st.Pool).CreateProjectRating(ctx, queries.CreateProjectRatingParams{
		SubmissionID: submissionId,
		IsDraft:      true,
		Type:         RatingTypesMappingRev[rtype],
	})

	if err != nil {
		return nil, fmt.Errorf("creating new submission rating: %w", err)
	}

	return &pb.Rating{
		RatingId:   r.RatingID,
		AssessorId: r.AssessorID,
		IsDraft:    r.IsDraft,
		Type:       rtype,
	}, nil
}

// UpsertPartialRatings either creates a new partial rating or updates an existing one based on the given PartialRatingRequest.PartialRatingID.
// If the PartialRatingRequest.PartialRatingID is 0, a new partial rating is created.
// If the PartialRatingRequest.PartialRatingID is not 0, the existing partial rating is updated.
func (st *Store) UpsertPartialRatings(ctx context.Context, in *pb.PartialRatingRequest) (*pb.PartialRating, error) {

	var (
		err      error
		ratingId = in.GetRatingId()
	)
	// For updates we don't have actual ratingId at hand, so...
	// for checking access we have to get it from partial rating
	if in.GetPartialRatingId() != 0 {

		if ratingId, err = queries.New(st.Pool).GetRatingIdForPartialRating(ctx, in.GetPartialRatingId()); err != nil {

			// allowing for errors == pgx.ErrNoRows to go this route
			return nil, fmt.Errorf("getting rating id for partial rating: %w", err)
		}
	}

	hasAccess, err := queries.New(st.Pool).DoesAssessorHaveAccessToRating(ctx, NewRatingsParams{AssessorID: in.GetAssessorId(), RatingID: ratingId})
	if err != nil {
		return nil, fmt.Errorf("checking access: %w", err)
	}
	if !hasAccess {
		return nil, fmt.Errorf("User does not have access to resource")
	}

	// If we have partial rating id we update existing
	if in.GetPartialRatingId() != 0 {

		// First let's check for condition races and let's make sure the rating we are about to update exists:
		// 1. Parse time from request
		lastModified, err := time.Parse(time.RFC3339, in.GetXModified())
		if err != nil {
			return nil, fmt.Errorf("parsing modified time: %w", err)
		}

		// 2. Check if partial rating exists and compare timestamps. If all goes well result should be empty string.
		result, err := queries.New(st.Pool).ValidatePartialRating(ctx, ValidationParams{
			PartialRatingID: in.GetPartialRatingId(),
			Modified:        lastModified,
		})

		if err != nil {
			return nil, fmt.Errorf("checking if partial rating exists: %w", err)
		}

		// if rating with that id does not exist result will be equal to string 'id';
		if result == "id" {
			return nil, fmt.Errorf("partial rating with id %d does not exist", in.GetPartialRatingId())
		}

		// if timestamps are different result will be equal to string representation of timestamp;
		if result != "" {
			// This means we detected condition race.
			// We parse the error to make sure it's a valid timestamp -- if not this is a bug.
			if _, err := time.Parse(time.RFC3339, result); err != nil {
				st.Log.Warn("failed to parse modified time from db", zap.String("time_from_db", result))
			}

			return nil, RaceConditionDetectedErr
		}

		ret, err := queries.New(st.Pool).UpdatePartialRating(ctx, queries.UpdatePartialRatingParams{
			PartialRatingID: in.GetPartialRatingId(),
			Points:          in.GetPoints(),
			Justification:   in.GetJustification(),
			ModifiedByID:    in.GetAssessorId(),
		})

		if err != nil {
			return nil, fmt.Errorf("updating partial rating with id %d: %w", in.GetPartialRatingId(), err)
		}

		return &pb.PartialRating{
			PartialRatingId: ret.PartialRatingID,
			CriterionId:     ret.CriterionID,
			Points:          ret.Points,
			Justification:   ret.Justification,
			Modified:        ret.Modified.Format(time.RFC3339),
			ModifiedBy:      ret.ModifiedByID,
		}, nil
	}

	// Otherwise we create new partial rating
	ret, err := queries.New(st.Pool).CreatePartialRating(ctx, queries.CreatePartialRatingParams{
		RatingID:      in.GetRatingId(),
		CriterionID:   in.GetCriterionId(),
		Points:        in.GetPoints(),
		Justification: in.GetJustification(),
		ModifiedByID:  in.GetAssessorId(),
	})

	if err != nil {
		return nil, fmt.Errorf("creating partial rating: %w", err)
	}

	return &pb.PartialRating{
		PartialRatingId: ret.PartialRatingID,
		CriterionId:     ret.CriterionID,
		Points:          ret.Points,
		Justification:   ret.Justification,
		Modified:        ret.Modified.Format(time.RFC3339),
		ModifiedBy:      ret.ModifiedByID,
	}, nil
}
