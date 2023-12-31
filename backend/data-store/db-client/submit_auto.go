package dbclient

import (
	"context"
	"errors"
	"time"
	queries "zpi/sql/gen"

	"go.uber.org/zap"
)

// WatchForAutoSubmit watches forver (until context cancellation) for ratings that should be autosubmitted
func (st *Store) WatchForAutoSubmit(ctx context.Context) {
	ticker := time.NewTicker(5 * time.Minute)
	log := st.Log.Named("autosubmiter")

	for {
		select {
		case <-ctx.Done():
			log.Info("context cancelled, exiting autosubmiter")
			return
		case <-ticker.C:
			log.Info("checking for autosubmit")
			if err := st.AutoSubmit(ctx, log); err != nil {
				log.Error("error autosubmitting", zap.Error(err))
			}
		}
	}
}

// AutoSubmit tries to autosubmit all ratings that are over the deadline
func (st *Store) AutoSubmit(ctx context.Context, log *zap.Logger) error {
	ctx, cancel := context.WithTimeout(ctx, 60*time.Second)
	defer cancel()

	ratingsToTryAutoSubmit, err := queries.New(st.Pool).GetDelayedRatings(ctx)
	if err != nil {
		return err
	}

	errs := []error{}
	for _, ratingId := range ratingsToTryAutoSubmit {

		log.Info("trying to autosubmit", zap.Int32("rating_id", ratingId))

		if _, err := st.SubmitRatingPlain(ctx, ratingId); err != nil {
			log.Info("error submitting rating", zap.Error(err))

			errs = append(errs, err)
		}

		log.Info("autosubmitted successfully", zap.Int32("rating_id", ratingId))
	}

	if len(errs) > 0 {
		return errors.Join(errs...)
	}
	return nil
}
