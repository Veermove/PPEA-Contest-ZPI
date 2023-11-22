package main

import (
	"context"
	"fmt"
	"regexp"
	dbclient "zpi/db-client"
	ds "zpi/pb"

	"go.uber.org/zap"
)

type DataStore struct {
	ds.UnimplementedDataStoreServer

	Log *zap.Logger
	Db  *dbclient.Store
}

var (
	_ ds.DataStoreServer = (*DataStore)(nil)

	emailRegex = regexp.MustCompile(`^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$`)
)

func (s *DataStore) GetSubmissions(ctx context.Context, req *ds.SubmissionRequest) (*ds.SubmissionsResponse, error) {
	if req.GetAssessorId() == 0 {
		return nil, fmt.Errorf("assessor id is required")
	}
	s.Log.Info("getting submissions", zap.Int32("assessor_id", req.GetAssessorId()))
	return s.Db.GetSubmissionsByAssessor(ctx, req.GetAssessorId())
}

func (s *DataStore) GetSubmissionDetails(ctx context.Context, req *ds.DetailsSubmissionRequest) (*ds.DetailsSubmissionResponse, error) {
	if req.GetSubmissionId() == 0 {
		return nil, fmt.Errorf("submission id is required")
	}

	s.Log.Info("getting submission details",
		zap.Int32("submission_id", req.GetSubmissionId()),
		zap.Int32("assessor_id", req.GetAssessorId()))

	return s.Db.GetSubmissionDetails(ctx, req.GetSubmissionId(), req.GetAssessorId())
}

func (s *DataStore) GetSubmissionRatings(ctx context.Context, req *ds.RatingsSubmissionRequest) (*ds.RatingsSubmissionResponse, error) {
	if req.GetSubmissionId() == 0 {
		return nil, fmt.Errorf("submission id is required")
	}
	s.Log.Info("getting submission ratings",
		zap.Int32("submission_id", req.GetSubmissionId()),
		zap.Int32("assessor_id", req.GetAssessorId()))

	return s.Db.GetSubmissionRatings(ctx, req.GetSubmissionId(), req.GetAssessorId())
}

func (s *DataStore) GetUserClaims(ctx context.Context, req *ds.UserRequest) (*ds.UserClaimsResponse, error) {
	if !emailRegex.MatchString(req.GetEmail()) {
		return nil, fmt.Errorf("invalid email address")
	}

	s.Log.Info("getting user claims", zap.String("email", req.GetEmail()))

	return s.Db.GetUserClaims(ctx, req.GetEmail())
}

func (s *DataStore) PostNewSubmissionRating(ctx context.Context, in *ds.NewSubmissionRatingRequest) (*ds.Rating, error) {
	if in.GetAssessorId() == 0 {
		return nil, fmt.Errorf("assessor id is required")
	}
	if in.GetSubmissionId() == 0 {
		return nil, fmt.Errorf("submission id is required")
	}

	s.Log.Info("creating new submission rating",
		zap.Int32("assessor_id", in.GetAssessorId()),
		zap.Int32("submission_id", in.GetSubmissionId()))

	return s.Db.CreateNewSubmissionRating(ctx, in.GetAssessorId(), in.GetSubmissionId(), in.GetType())
}

func (s *DataStore) PostPartialRating(ctx context.Context, in *ds.PartialRatingRequest) (*ds.PartialRating, error) {
	if in.GetPartialRatingId() == 0 && (in.GetCriterionId() == 0 || in.GetRatingId() == 0) {
		return nil, fmt.Errorf("not enough information to post partial rating")
	}

	s.Log.Info("creating new partial rating",
		zap.Int32("assessor_id", in.GetAssessorId()),
		zap.Int32("rating_id", in.GetRatingId()),
		zap.Int32("criterion_id", in.GetCriterionId()))

	return s.Db.CreateNewPartialRating(ctx, in)
}

func (s *DataStore) PostSubmitRating(ctx context.Context, in *ds.SubmitRatingDraft) (*ds.Rating, error) {
	if in.GetRatingId() == 0 || in.GetAssessorId() == 0 {
		return nil, fmt.Errorf("cannot submit rating with assessorId = %d and ratingId = %d", in.GetRatingId(), in.GetAssessorId())
	}

	s.Log.Info("submitting rating", zap.Int32("ratingId", in.GetRatingId()), zap.Int32("assessorId", in.GetAssessorId()))

	return s.Db.SubmitRating(ctx, in)
}

func (s *DataStore) GetStudyVisits(ctx context.Context, in *ds.StudyVisitRequest) (*ds.StudyVisitResponse, error) {
	if in.GetAssessorId() == 0 {
		return nil, fmt.Errorf("assessor id is required")
	}
	if in.GetSubmissionId() == 0 {
		return nil, fmt.Errorf("submission id is required")
	}

	return s.Db.GetStudyVisits(ctx, in.GetAssessorId(), in.GetSubmissionId())
}
