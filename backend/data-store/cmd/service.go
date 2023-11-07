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
		return nil, fmt.Errorf("assessor email is required")
	}
	return s.Db.GetSubmissionsByAssessor(ctx, req.GetAssessorId())
}

func (s *DataStore) GetSubmissionDetails(ctx context.Context, req *ds.DetailsSubmissionRequest) (*ds.DetailsSubmissionResponse, error) {
	if req.GetSubmissionId() == 0 {
		return nil, fmt.Errorf("submission id is required")
	}
	return s.Db.GetSubmissionDetails(ctx, req.GetSubmissionId(), req.GetAssessorId())
}

func (s *DataStore) GetSubmissionRatings(ctx context.Context, req *ds.RatingsSubmissionRequest) (*ds.RatingsSubmissionResponse, error) {
	if req.GetSubmissionId() == 0 {
		return nil, fmt.Errorf("submission id is required")
	}
	return s.Db.GetSubmissionRatings(ctx, req.GetSubmissionId(), req.GetAssessorId())
}

func (s *DataStore) GetUserClaims(ctx context.Context, req *ds.UserRequest) (*ds.UserClaimsResponse, error) {
	if !emailRegex.MatchString(req.GetEmail()) {
		return nil, fmt.Errorf("invalid email address")
	}
	s.Log.Info("getting user claims", zap.String("email", req.GetEmail()))
	return s.Db.GetUserClaims(ctx, req.GetEmail())
}
