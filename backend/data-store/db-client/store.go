package dbclient

import (
	"context"
	"fmt"
	"os"
	"strings"
	"time"

	"github.com/jackc/pgx/v4/pgxpool"
	"go.uber.org/zap"
)

const (
	MaxPgConn = 10
	MinPgConn = 3
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

func (st *Store) Close() {
	st.Pool.Close()
}
