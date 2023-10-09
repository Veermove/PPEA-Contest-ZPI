package dbclient

import (
	"embed"
	"fmt"

	"github.com/golang-migrate/migrate/v4"
	"github.com/golang-migrate/migrate/v4/source"
	"github.com/golang-migrate/migrate/v4/source/iofs"

	_ "github.com/golang-migrate/migrate/v4/database/postgres"
	"go.uber.org/zap"
)

//go:embed migrations
var migrations embed.FS

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
		return fmt.Errorf("running migration - new with source instance: %w", err)
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
