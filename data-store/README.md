## On the subject of migrations

Tool responsible for migrations:
```bash
$ go install -tags 'postgres' github.com/golang-migrate/migrate/v4/cmd/migrate@latest
```

Display version of last applied migration to databse:
```bash
$ migrate \
    -path ./db-client/migrations/ \
    -database postgres://${PG_STORE_USER}:${PG_STORE_PASSWORD}@${PG_STORE_HOST}:${PG_STORE_PORT}/${PG_STORE_DATABASE}?sslmode=disable
    version

$ migrate \
    -path ./db-client/migrations/ \
    -database postgres://postgres:postgres@localhost:5431/datastore?sslmode=disable version
```

Apply all "up" migrations:
```bash
$ migrate \
    -path ./db-client/migrations/ \
    -database ...
    up
```

Apply all "down" migrations:
```bash
$ migrate \
    -path ./db-client/migrations/ \
    -database ...
    down
```

### On the subject of adding new migrations
As per documentation:
```bash
$ create [-ext E] [-dir D] [-seq] [-digits N] [-format] [-tz] NAME
           Create a set of timestamped up/down migrations titled NAME, in directory D with extension E.
           Use -seq option to generate sequential up/down migrations with N digits.
           Use -format option to specify a Go time format string. Note: migrations with the same time cause "duplicate migration version" error.
           Use -tz option to specify the timezone that will be used when generating non-sequential migrations (defaults: UTC).
```

