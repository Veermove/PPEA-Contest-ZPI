package main

import (
	"context"
	"fmt"
	dbclient "zpi/db-client"
	ds "zpi/pb"

	"go.uber.org/zap"
)

type DataStore struct {
	ds.UnimplementedDataStoreServer

	Log *zap.Logger
	Db  *dbclient.Store
}

var _ ds.DataStoreServer = (*DataStore)(nil)

func (s *DataStore) GetSubmissions(ctx context.Context, req *ds.SubmissionRequest) (*ds.SubmissionsResponse, error) {
	if req.GetAssessorId() == 0 {
		return nil, fmt.Errorf("assessor id is required")
	}
	return s.Db.GetSubmissionsByAssessor(ctx, req.GetAssessorId())
}

func (s *DataStore) GetSubmissionDetails(ctx context.Context, req *ds.DetailsSubmissionRequest) (*ds.DetailsSubmissionResponse, error) {
	if req.GetSubmissionId() == 0 {
		return nil, fmt.Errorf("submission id is required")
	}
	return s.Db.GetSubmissionDetails(ctx, req.GetSubmissionId())
}

func (s *DataStore) GetSubmissionRatings(ctx context.Context, req *ds.RatingsSubmissionRequest) (*ds.RatingsSubmissionResponse, error) {
	panic("TODO: GetSubmissionRatings")
}
