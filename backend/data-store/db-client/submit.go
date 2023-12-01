package dbclient

import (
	"context"
	"fmt"
	pb "zpi/pb"
	queries "zpi/sql/gen"
)

func (st *Store) SubmitRating(ctx context.Context, in *pb.SubmitRatingDraft) (*pb.Rating, error) {
	hasAccess, err := queries.New(st.Pool).DoesAssessorHaveAccessToRating(ctx, NewRatingsParams{AssessorID: in.GetAssessorId(), RatingID: in.GetRatingId()})
	if err != nil {
		return nil, fmt.Errorf("checking access: %w", err)
	}
	if !hasAccess {
		return nil, fmt.Errorf("User does not have access to resource")
	}

	return st.SubmitRatingPlain(ctx, in.GetRatingId())
}

func (st *Store) SubmitRatingPlain(ctx context.Context, ratingId int32) (*pb.Rating, error) {
	allPartialRatingsSubmitted, err := queries.New(st.Pool).CheckAllPartialRatingsSubmitted(ctx, ratingId)
	if err != nil {
		return nil, fmt.Errorf("checking if all p-ratings are submitted")
	}

	if !allPartialRatingsSubmitted {
		return nil, fmt.Errorf("rating cannot be submitted because not all partial rating were present")
	}

	// start transaction
	tx, err := st.Pool.Begin(ctx)
	// if we commit transaction before the end this becomes a no-op
	defer tx.Rollback(ctx)

	if err != nil {
		return nil, fmt.Errorf("starting transaction: %w", err)
	}

	// submit the rating
	ret, err := queries.New(tx).SubmitRating(ctx, queries.SubmitRatingParams{
		IsDraft:  false,
		RatingID: ratingId,
	})
	if err != nil {
		return nil, fmt.Errorf("submitting rating: %w", err)
	}

	// check condition and set next status
	if err := queries.New(tx).SetNextStatus(ctx, ret.SubmissionID); err != nil {
		return nil, fmt.Errorf("setting next status for submission: %w", err)
	}

	if err := tx.Commit(ctx); err != nil {
		return nil, fmt.Errorf("committing transaction: %w", err)
	}

	return &pb.Rating{
		RatingId:   ret.RatingID,
		IsDraft:    ret.IsDraft,
		AssessorId: ret.AssessorID,
		Type:       RatingTypesMapping[ret.Type],
	}, nil
}
