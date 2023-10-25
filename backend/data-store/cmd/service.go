package main

import (
	"context"
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

func (s *DataStore) GetSubmissions(context.Context, *ds.SubmissionRequest) (*ds.SubmissionsResponse, error) {
	panic("TODO: GetSubmissions")
}

func (s *DataStore) GetSubmissionDetails(context.Context, *ds.DetailsSubmissionRequest) (*ds.DetailsSubmissionResponse, error) {
	panic("TODO: GetSubmissionDetails")
}

func (s *DataStore) GetSubmissionRatings(context.Context, *ds.RatingsSubmissionRequest) (*ds.RatingsSubmissionResponse, error) {
	panic("TODO: GetSubmissionRatings")
}
