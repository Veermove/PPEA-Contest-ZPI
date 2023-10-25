package dbclient

import (
	"context"
	"database/sql"
	"fmt"
	"os"
	"strings"
	"time"

	ds "zpi/pb"
	queries "zpi/sql"

	"github.com/jackc/pgx/v4"
	"github.com/jackc/pgx/v4/pgxpool"
	"github.com/sourcegraph/conc/iter"
	"go.uber.org/zap"
)

const (
	MaxPgConn = 10
	MinPgConn = 3
)

var (
	RatingTypesMapping = map[queries.ProjectRatingType]ds.RatingType{
		queries.ProjectRatingTypeIndividual: ds.RatingType_INDIVIDUAL,
		queries.ProjectRatingTypeInitial:    ds.RatingType_INITIAL,
		queries.ProjectRatingTypeFinal:      ds.RatingType_FINAL,
	}
	SubmissionStatesTypesMapping = map[queries.ProjectState]ds.ProjectState{
		queries.ProjectStateDraft:     ds.ProjectState_DRAFT,
		queries.ProjectStateSubmitted: ds.ProjectState_SUBMITTED,
		queries.ProjectStateAccepted:  ds.ProjectState_ACCEPTED,
		queries.ProjectStateRejected:  ds.ProjectState_REJECTED,
	}
)

type Store struct {
	Pool *pgxpool.Pool
}

func GetConnectionString() string {
	return fmt.Sprintf("postgres://%s:%s@%s:%s/%s?sslmode=disable",
		os.Getenv("PG_STORE_USER"),
		os.Getenv("PG_STORE_PASSWORD"),
		os.Getenv("PG_STORE_HOST"),
		os.Getenv("PG_STORE_PORT"),
		os.Getenv("PG_STORE_DATABASE"),
	)
}

func Open(ctx context.Context, log *zap.Logger) (*Store, error) {
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

	return store, nil
}

func (st *Store) GetSubmissionDetails(ctx context.Context, submissionId int32) (*ds.DetailsSubmissionResponse, error) {
	details, err := queries.New(st.Pool).GetSubmissionDetails(ctx, submissionId)
	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	budget, err := details.Budget.EncodeText(nil, []byte{})

	if err != nil { // is it normal that column is in undefined state?
		budget = []byte{} // hope so
	}

	var submissionDate string
	if details.ReportSubmissionDate.Valid && details.ReportSubmissionDate.Time != (time.Time{}) {
		submissionDate = details.ReportSubmissionDate.Time.Format(time.RFC3339)
	} else {
		submissionDate = ""
	}

	return &ds.DetailsSubmissionResponse{
		TeamSize:    details.TeamSize,
		FinishDate:  details.FinishDate.Format(time.RFC3339),
		Status:      SubmissionStatesTypesMapping[details.Status],
		Budget:      string(budget),
		Description: details.Description,
		Report: &ds.AppReport{
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

func (st *Store) GetSubmissionsByAssessor(ctx context.Context, assessorId int32) (*ds.SubmissionsResponse, error) {
	returnVal := &ds.SubmissionsResponse{Submissions: []*ds.Submission{}}

	subs, err := queries.New(st.Pool).GetSubmissionsByAssessorId(ctx, assessorId)

	if err == pgx.ErrNoRows {
		return returnVal, nil
	}
	if err != nil {
		return nil, err
	}

	submissions, err := iter.MapErr(subs, func(subm *queries.GetSubmissionsByAssessorIdRow) (*ds.Submission, error) {
		submission := &ds.Submission{
			SubmissionId: subm.SubmissionID,
			Year:         subm.Year,
			Name:         subm.Name,
			DurationDays: subm.DurationDays,
		}

		assessors, errAs := queries.New(st.Pool).GetAssessorsForSubmission(ctx, subm.SubmissionID)

		if errAs != nil && errAs != pgx.ErrNoRows {
			return nil, errAs
		}

		ratings, errRt := queries.New(st.Pool).GetRatingsForSubission(ctx, subm.SubmissionID)

		if errRt != nil && errRt != pgx.ErrNoRows {
			return nil, errRt
		}

		submission.Assessors = MapAssessorsFromSql(assessors)
		submission.Ratings = MapRatingsFromSql(ratings)
		return submission, nil
	})

	if err != nil {
		return nil, err
	}
	return &ds.SubmissionsResponse{Submissions: submissions}, nil
}

func MapRatingsFromSql(rts []queries.GetRatingsForSubissionRow) (ratings []*ds.Rating) {
	for _, rt := range rts {
		ratings = append(ratings, &ds.Rating{
			RatingId:   rt.RatingID,
			AssessorId: rt.AssessorID,
			IsDraft:    rt.IsDraft,
			Type:       RatingTypesMapping[rt.Type],
		})
	}
	return
}

func MapAssessorsFromSql(ass []queries.GetAssessorsForSubmissionRow) (asses []*ds.Assessor) {
	for _, a := range ass {
		asses = append(asses, &ds.Assessor{
			FirstName:  a.FirstName,
			LastName:   a.LastName,
			AssessorId: a.AssessorID,
		})
	}
	return
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
