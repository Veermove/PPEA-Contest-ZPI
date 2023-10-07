package dbclient

import (
	"context"
	"fmt"
	"strings"
	"time"
	envvar "zpi/data-store/cmd/env"

	"github.com/jackc/pgx/v4/pgxpool"
)

const (
	MaxPgConn = 10
	MinPgConn = 3
)

type Store struct {
	Pool *pgxpool.Pool
}

func Open(ctx context.Context) (*Store, error) {
	var (
		confs = strings.Join([]string{
			fmt.Sprintf("%s=%s", "dbname", envvar.GetOrPanic("PG_STORE_DATABASE")),
			fmt.Sprintf("%s=%s", "user", envvar.GetOrPanic("PG_STORE_USER")),
			fmt.Sprintf("%s=%s", "password", envvar.GetOrPanic("PG_STORE_PASSWORD")),
			fmt.Sprintf("%s=%s", "host", envvar.GetOrPanic("PG_STORE_HOST")),
			fmt.Sprintf("%s=%s", "port", envvar.GetOrPanic("PG_STORE_PORT")),
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
