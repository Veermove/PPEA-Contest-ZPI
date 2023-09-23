package dbclient

import (
	"context"
	"fmt"
	"strings"
	"time"

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
			fmt.Sprintf("%s=%s", "dbname", "store"),   //os.Getenv("ZPI_DATASTORE_NAME")),
			fmt.Sprintf("%s=%s", "user", "keeper"),    //os.Getenv("ZPI_DATASTORE_USER")),
			fmt.Sprintf("%s=%s", "password", "admin"), //os.Getenv("ZPI_DATASTORE_PASSWORD")),
			fmt.Sprintf("%s=%s", "host", "localhost"), //os.Getenv("ZPI_DATASTORE_HOST")),
			fmt.Sprintf("%s=%d", "port", 5431),        //os.Getenv("ZPI_DATASTORE_PORT")),
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
