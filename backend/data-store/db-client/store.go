package dbclient

import (
	"context"
	"database/sql"
	"embed"
	"fmt"
	"os"
	"strings"
	"time"

	pb "zpi/pb"
	queries "zpi/sql/gen"

	"github.com/golang-migrate/migrate/v4"
	"github.com/golang-migrate/migrate/v4/source"
	"github.com/golang-migrate/migrate/v4/source/iofs"
	"github.com/jackc/pgx/v4/pgxpool"
	"go.uber.org/zap"

	_ "github.com/golang-migrate/migrate/v4/database/postgres"
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
	RatingTypesMappingRev = map[pb.RatingType]queries.ProjectRatingType{
		pb.RatingType_INDIVIDUAL: queries.ProjectRatingTypeIndividual,
		pb.RatingType_INITIAL:    queries.ProjectRatingTypeInitial,
		pb.RatingType_FINAL:      queries.ProjectRatingTypeFinal,
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
		Log  *zap.Logger
	}
	AccessParams     = queries.DoesAssessorHaveAccessParams
	NewRatingsParams = queries.DoesAssessorHaveAccessToRatingParams
	ValidationParams = queries.ValidatePartialRatingParams
)

//go:embed migrations
var migrations embed.FS

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

	store := &Store{Log: log.Named("db-client")}
	store.Pool, err = pgxpool.ConnectConfig(ctx, config)
	if err != nil {
		return nil, err
	}

	if init_dict {
		log.Info("initializing dictionary data")
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
				return nil, fmt.Errorf("executing init.sql, stmt %d : %w \n%s\n", i, err, stmt)
			}
		}
	}

	return store, nil
}

func RunMigrations(log *zap.Logger) error {
	var (
		connStr = GetConnectionString()
		err     error
		dir     source.Driver
	)

	if dir, err = iofs.New(migrations, "migrations"); err != nil {
		return fmt.Errorf("running migration - new iofs: %w", err)
	}

	var migs *migrate.Migrate
	if migs, err = migrate.NewWithSourceInstance("iofs", dir, connStr); err != nil {
		return fmt.Errorf("running migration - new with source instance, conn str: %s : %w", connStr, err)
	}

	if err = migs.Up(); err == migrate.ErrNoChange {
		log.Info("no migrations to run")
		return nil
	} else if err != nil {
		return err
	}
	log.Info("migrations ran successfully")

	return nil
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
