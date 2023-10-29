package dbclient

import (
	"context"
	"database/sql"
	"fmt"
	"os"
	"strings"
	"time"

	pb "zpi/pb"
	queries "zpi/sql/gen"

	"github.com/jackc/pgx/v4"
	"github.com/jackc/pgx/v4/pgxpool"
	"github.com/samber/lo"
	"github.com/sourcegraph/conc/iter"
	"github.com/sourcegraph/conc/pool"
	"go.uber.org/zap"
)

const (
	MaxPgConn = 10
	MinPgConn = 3
)

var (
	RatingTypesMapping = map[queries.ProjectRatingType]pb.RatingType{
		queries.ProjectRatingTypeIndividual: pb.RatingType_INDIVIDUAL,
		queries.ProjectRatingTypeInitial:    pb.RatingType_INITIAL,
		queries.ProjectRatingTypeFinal:      pb.RatingType_FINAL,
	}
	SubmissionStatesTypesMapping = map[queries.ProjectState]pb.ProjectState{
		queries.ProjectStateDraft:     pb.ProjectState_DRAFT,
		queries.ProjectStateSubmitted: pb.ProjectState_SUBMITTED,
		queries.ProjectStateAccepted:  pb.ProjectState_ACCEPTED,
		queries.ProjectStateRejected:  pb.ProjectState_REJECTED,
	}
)

type (
	Store struct {
		Pool *pgxpool.Pool
	}

	Promise[T any] struct {
		Result chan *Result[T]
	}

	Result[T any] struct {
		Err error
		Val T
	}
)

func GetConnectionString() string {
	return fmt.Sprintf("postgres://%s:%s@%s:%s/%s?sslmode=disable",
		os.Getenv("PG_STORE_USER"),
		os.Getenv("PG_STORE_PASSWORD"),
		os.Getenv("PG_STORE_HOST"),
		os.Getenv("PG_STORE_PORT"),
		os.Getenv("PG_STORE_DATABASE"),
	)
}

func Open(ctx context.Context, log *zap.Logger, init_dict bool) (*Store, error) {
	if migErr := RunMigrations(log); migErr != nil {
		return nil, fmt.Errorf("running migrations: %w", migErr)
	}

	var (
		confs = strings.Join([]string{
			fmt.Sprintf("%s=%s", "dbname", os.Getenv("PG_STORE_DATABASE")),
			fmt.Sprintf("%s=%s", "user", os.Getenv("PG_STORE_USER")),
			fmt.Sprintf("%s=%s", "password", os.Getenv("PG_STORE_PASSWORD")),
			fmt.Sprintf("%s=%s", "host", os.Getenv("PG_STORE_HOST")),
			fmt.Sprintf("%s=%s", "port", os.Getenv("PG_STORE_PORT")),
		}, " ")
		config, err = pgxpool.ParseConfig(confs)
	)

	if err != nil {
		return nil, err
	}

	config.LazyConnect = false
	config.MaxConns = MaxPgConn
	config.MinConns = MinPgConn
	config.MaxConnLifetime = 2 * time.Minute

	store := &Store{}
	store.Pool, err = pgxpool.ConnectConfig(ctx, config)
	if err != nil {
		return nil, err
	}

	if init_dict {
		conn, err := store.Pool.Acquire(ctx)
		if err != nil {
			return nil, fmt.Errorf("acquiring connection for init dict: %w", err)
		}
		defer conn.Release()
		b, err := os.ReadFile("sql/init/init.sql")
		if err != nil {
			return nil, fmt.Errorf("reading init.sql: %w", err)
		}

		for i, stmt := range strings.Split(string(b), ";") {
			_, err = conn.Exec(ctx, stmt)

			if err != nil {
				return nil, fmt.Errorf("executing init.sql, stmt %d : %w", i, err)
			}
		}

	}

	return store, nil
}

func (st *Store) GetSubmissionDetails(ctx context.Context, submissionId int32) (*pb.DetailsSubmissionResponse, error) {
	details, err := queries.New(st.Pool).GetSubmissionDetails(ctx, submissionId)
	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("getting submission details: %w", err)
	}

	var submissionDate string
	if details.ReportSubmissionDate.Valid && details.ReportSubmissionDate.Time != (time.Time{}) {
		submissionDate = details.ReportSubmissionDate.Time.Format(time.RFC3339)
	} else {
		submissionDate = ""
	}

	return &pb.DetailsSubmissionResponse{
		TeamSize:    details.TeamSize,
		FinishDate:  details.FinishDate.Format(time.RFC3339),
		Status:      SubmissionStatesTypesMapping[details.Status],
		Budget:      details.SubmissionBudget,
		Description: details.Description,
		Report: &pb.AppReport{
			IsDraft:               details.IsDraft,
			ReportSubmissionDate:  submissionDate,
			ProjectGoals:          Denullify(details.ProjectGoals),
			OrganisationStructure: Denullify(details.OrganisationStructure),
			DivisionOfWork:        Denullify(details.DivisionOfWork),
			ProjectSchedule:       Denullify(details.ProjectSchedule),
			Attatchments:          Denullify(details.Attatchments),
		},
	}, nil
}

func (st *Store) GetSubmissionsByAssessor(ctx context.Context, assessorEmail string) (*pb.SubmissionsResponse, error) {
	returnVal := &pb.SubmissionsResponse{Submissions: []*pb.Submission{}}

	subs, err := queries.New(st.Pool).GetSubmissionsByAssessorEmail(ctx, assessorEmail)

	if err == pgx.ErrNoRows {
		return returnVal, nil
	}
	if err != nil {
		return nil, fmt.Errorf("getting submissions by assessor id: %w", err)
	}

	submissions, err := iter.MapErr(subs, func(subm *queries.GetSubmissionsByAssessorEmailRow) (*pb.Submission, error) {
		submission := &pb.Submission{
			SubmissionId: subm.SubmissionID,
			Year:         subm.Year,
			Name:         subm.Name,
			DurationDays: subm.DurationDays,
		}

		assessors, errAs := queries.New(st.Pool).GetAssessorsForSubmission(ctx, subm.SubmissionID)

		if errAs != nil && errAs != pgx.ErrNoRows {
			return nil, fmt.Errorf("getting assessors for submission: %w", errAs)
		}

		ratings, errRt := queries.New(st.Pool).GetRatingsForSubission(ctx, subm.SubmissionID)

		if errRt != nil && errRt != pgx.ErrNoRows {
			return nil, fmt.Errorf("getting ratings for submission: %w", errRt)
		}

		submission.Assessors = MapAssessorsFromSql(assessors)
		submission.Ratings = MapRatingsFromSql(ratings)
		return submission, nil
	})

	if err != nil {
		return nil, err
	}
	return &pb.SubmissionsResponse{Submissions: submissions}, nil
}

func (st *Store) GetSubmissionRatings(ctx context.Context, submissionId int32) (*pb.RatingsSubmissionResponse, error) {

	// Run query to get slice of
	// * AssessorID
	// * RatingID
	// * RatingType
	assessorsWithRating, err := queries.New(st.Pool).GetAssessorsAndRatingsForSubmission(ctx, submissionId)

	if err != nil && err != pgx.ErrNoRows {
		return nil, fmt.Errorf("getting assessors and ratings for submission: %w", err)
	}

	var (
		// Group by rating type ==> map[RatingType]        ->  []struct{ AssessorID, RatingID }
		// in golang types      ==> map[ProjectRatingType] ->  []*GetAssessorsAndRatingsForSubmissionRow
		ratingsByType = lo.GroupBy(
			lo.Map(assessorsWithRating, func(a queries.GetAssessorsAndRatingsForSubmissionRow, _ int) *queries.GetAssessorsAndRatingsForSubmissionRow {
				return &a
			}),
			func(a *queries.GetAssessorsAndRatingsForSubmissionRow) queries.ProjectRatingType {
				return a.RatingType
			},
		)

		// return value
		returnVal = &pb.RatingsSubmissionResponse{}

		// pool of workers for the rest of logic
		workers = pool.New().
			WithContext(ctx).
			WithMaxGoroutines(4).
			WithCancelOnError().
			WithFirstError()

		invidualRatingsHandler = func(ctx context.Context) error {
			individual, _ := ratingsByType[queries.ProjectRatingTypeIndividual]
			individualRs := []*pb.AssessorRatings{}

			// For each struct{ AssessorID, RatingID } ...
			for _, i := range individual {

				ast, err := st.CreateAssessorRatings(ctx,
					i.RatingID,
					i.AssessorID,
					i.FirstName,
					i.LastName,
				)
				if err != nil && err != pgx.ErrNoRows {
					return fmt.Errorf("creating individual rating for rating %d, assessor %d: %w", i.RatingID, i.AssessorID, err)
				}

				// ... and append to individualRs.
				individualRs = append(individualRs, ast)

			}

			returnVal.Individual = individualRs
			return nil
		}

		initRatingsHandler = func(ctx context.Context) error {

			initials, _ := ratingsByType[queries.ProjectRatingTypeInitial]

			// Generally we expect only one initial rating per submission
			if len(initials) > 1 {
				return fmt.Errorf("more than one initial rating for submission %d", submissionId)

			} else if len(initials) > 0 {
				// Get initial rating
				initial, err := st.CreateAssessorRatings(ctx,
					initials[0].RatingID,
					initials[0].AssessorID,
					initials[0].FirstName,
					initials[0].LastName,
				)

				if err != nil && err != pgx.ErrNoRows {
					return fmt.Errorf("creating initial rating for rating %d, assessor %d: %w", initials[0].RatingID, initials[0].AssessorID, err)
				}

				returnVal.Initial = initial
			}
			return nil
		}

		finalRratingsHandler = func(ctx context.Context) error {
			finals, _ := ratingsByType[queries.ProjectRatingTypeFinal]

			// Generally we expect only one final rating per submission
			if len(finals) > 1 {
				return fmt.Errorf("more than one final rating for submission %d", submissionId)

			} else if len(finals) > 0 {
				// Get final rating
				final, err := st.CreateAssessorRatings(ctx,
					finals[0].RatingID,
					finals[0].AssessorID,
					finals[0].FirstName,
					finals[0].LastName,
				)

				if err != nil {
					return fmt.Errorf("creating final rating for rating %d, assessor %d: %w", finals[0].RatingID, finals[0].AssessorID, err)
				}

				returnVal.Final = final
			}
			return nil
		}

		criteriaHandler = func(ctx context.Context) error {
			critsRaw, err := queries.New(st.Pool).GetCriteriaForSubmission(ctx, submissionId)
			if err != nil && err != pgx.ErrNoRows {
				return fmt.Errorf("getting criteria for submission: %w", err)
			}

			crits := []*pb.Criterion{}
			for _, c := range critsRaw {
				crits = append(crits, &pb.Criterion{
					CriterionId: c.PemCriterionID,
					Name:        c.Name,
					Description: c.Description,
					Area:        c.Area,
					Criteria:    c.Criteria,
					Subcriteria: Denullify(c.Subcriteria),
				})
			}

			returnVal.Criteria = crits
			return nil
		}
	)

	// Handle individual ratings
	workers.Go(invidualRatingsHandler)

	// Handle initial ratings
	workers.Go(initRatingsHandler)

	// Handle final ratings
	workers.Go(finalRratingsHandler)

	// Fetch criteria
	workers.Go(criteriaHandler)

	if err := workers.Wait(); err != nil {
		return nil, err
	}

	return returnVal, nil
}

// CreateAssessorRatings creates a new AssessorRatings object with the given rating ID, assessor ID, first name, and last name.
// It retrieves partial ratings for the given assessor and rating ID, and remaps the values to create the new AssessorRatings object.
// Returns the new AssessorRatings object and an error if there was any issue retrieving the partial ratings.
func (st *Store) CreateAssessorRatings(ctx context.Context, ratingId int32, assessorId int32, firstN, lastN string) (*pb.AssessorRatings, error) {

	// Get partial ratings for this assessor and rating id
	r, err := queries.New(st.Pool).GetPRatingsForAssessorAndRatingID(ctx,
		queries.GetPRatingsForAssessorAndRatingIDParams{RatingID: ratingId, AssessorID: assessorId},
	)
	if err != nil && err != pgx.ErrNoRows {
		return nil, fmt.Errorf("getting partial ratings for assessor %d and rating id %d: %w", assessorId, ratingId, err)
	}

	// Remap values
	initialRs := &pb.AssessorRatings{
		AssessorId:     assessorId,
		FirstName:      firstN,
		LastName:       lastN,
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

func MapRatingsFromSql(rts []queries.GetRatingsForSubissionRow) (ratings []*pb.Rating) {
	for _, rt := range rts {
		ratings = append(ratings, &pb.Rating{
			RatingId:   rt.RatingID,
			AssessorId: rt.AssessorID,
			IsDraft:    rt.IsDraft,
			Type:       RatingTypesMapping[rt.Type],
		})
	}
	return
}

func MapAssessorsFromSql(ass []queries.GetAssessorsForSubmissionRow) (asses []*pb.Assessor) {
	for _, a := range ass {
		asses = append(asses, &pb.Assessor{
			FirstName:  a.FirstName,
			LastName:   a.LastName,
			AssessorId: a.AssessorID,
		})
	}
	return
}

func DenullifyInt32(s sql.NullInt32) int32 {
	if !s.Valid || s.Int32 == 0 {
		return 0
	}
	return s.Int32
}

func Denullify(s sql.NullString) string {
	if !s.Valid {
		return ""
	}
	return s.String
}

func (st *Store) Close() {
	st.Pool.Close()
}
