package dbclient

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"sync"
	"time"
	ds "zpi/pb"
	queries "zpi/sql/gen"

	"github.com/samber/lo"
	"go.uber.org/zap"
)

type (
	EmailRow struct {
		AssessorID     int32
		SubmissionID   int32
		SubmissionName string
		Year           int32
		SetBeforeDate  sql.NullTime
		IsCreated      bool
		FirstName      string
		LastName       string
		Email          string

		XSetBeforeDate time.Time
	}
)

func (st *Store) GetEmailDetails(ct context.Context) (*ds.EmailResponse, error) {
	var (
		ctx, cancel       = context.WithTimeout(ct, 1*time.Minute)
		wg                = sync.WaitGroup{}
		individualRatings = []*EmailRow{}
		finalRatings      = []*EmailRow{}
		initialRatings    = []*EmailRow{}
	)
	defer cancel()
	wg.Add(3)

	go func() {
		defer wg.Done()
		emails, err := st.getEmailsWithBackoff(ctx, 3, st.getEmailsForFinalRatings)
		if err != nil {
			st.Log.Error("error getting emails", zap.Error(err))
			return
		}
		finalRatings = emails
	}()

	go func() {
		defer wg.Done()
		emails, err := st.getEmailsWithBackoff(ctx, 3, st.getEmailsForIndividualRatings)
		if err != nil {
			st.Log.Error("error getting emails", zap.Error(err))
			return
		}
		individualRatings = emails
	}()

	go func() {
		defer wg.Done()
		emails, err := st.getEmailsWithBackoff(ctx, 3, st.getEmailsForInitialRatings)
		if err != nil {
			st.Log.Error("error getting emails", zap.Error(err))
			return
		}
		individualRatings = emails
	}()

	wg.Wait()

	merged := append(append(individualRatings, finalRatings...), initialRatings...)
	emialDetails := make([]*ds.EmailDetails, 0, len(merged))

	for _, email := range merged {

		if !email.SetBeforeDate.Valid { // Unreachable if I'm sane
			st.Log.Error("email has no set before date", zap.Any("email", email))
			continue
		}

		email.XSetBeforeDate = email.SetBeforeDate.Time

		// suppose today       = 13.05
		// email.setBeforeDate = 15.05
		// then: setBeforeDate - EmailWarningPeriod (5 days) = 10.05
		// is 10.05 before 13.05? yes, so we should send email

		if !email.XSetBeforeDate.Add(-EmailWarningPeriod).Before(time.Now()) {
			continue
		}

		// check how many emails we sent?
	}

}

func (st *Store) getEmailsForIndividualRatings(ctx context.Context) ([]*EmailRow, error) {
	res, err := queries.New(st.Pool).GetIndividualRatingsEmails(ctx)
	if err != nil {
		return nil, fmt.Errorf("getting emails for individual ratings: %w", err)
	}

	return lo.Map(res, func(r queries.GetIndividualRatingsEmailsRow, _ int) *EmailRow {
		return &EmailRow{
			AssessorID:     r.AssessorID,
			SubmissionID:   r.SubmissionID,
			SubmissionName: r.SubmissionName,
			Year:           r.Year,
			SetBeforeDate:  r.SetBeforeDate,
			IsCreated:      r.IsCreated,
			FirstName:      r.FirstName,
			LastName:       r.LastName,
			Email:          r.Email,
		}
	}), nil
}

func (st *Store) getEmailsForFinalRatings(ctx context.Context) ([]*EmailRow, error) {
	res, err := queries.New(st.Pool).GetFinalRatingsEmails(ctx)
	if err != nil {
		return nil, fmt.Errorf("getting emails for final ratings: %w", err)
	}

	return lo.Map(res, func(r queries.GetFinalRatingsEmailsRow, _ int) *EmailRow {
		return &EmailRow{
			AssessorID:     r.AssessorID,
			SubmissionID:   r.SubmissionID,
			SubmissionName: r.SubmissionName,
			Year:           r.Year,
			SetBeforeDate:  r.SetBeforeDate,
			IsCreated:      r.IsCreated,
			FirstName:      r.FirstName,
			LastName:       r.LastName,
			Email:          r.Email,
		}
	}), nil
}

func (st *Store) getEmailsForInitialRatings(ctx context.Context) ([]*EmailRow, error) {
	res, err := queries.New(st.Pool).GetInitialRatingsEmails(ctx)
	if err != nil {
		return nil, fmt.Errorf("getting emails for initial ratings: %w", err)
	}

	return lo.Map(res, func(r queries.GetInitialRatingsEmailsRow, _ int) *EmailRow {
		return &EmailRow{
			AssessorID:     r.AssessorID,
			SubmissionID:   r.SubmissionID,
			SubmissionName: r.SubmissionName,
			Year:           r.Year,
			SetBeforeDate:  r.SetBeforeDate,
			IsCreated:      r.IsCreated,
			FirstName:      r.FirstName,
			LastName:       r.LastName,
			Email:          r.Email,
		}
	}), nil
}

func DenullifyTime(s sql.NullTime) time.Time {
	if s.Valid {
		return s.Time
	}
	// yeah, returning zero time is a good idea
	return time.Time{} // but to be honest, i dunno when it could be null
}

func (st *Store) getEmailsWithBackoff(
	ctx context.Context,
	backoff int,
	getter func(context.Context) ([]*EmailRow, error),
) ([]*EmailRow, error) {

	for i := 0; i < backoff; i++ {
		emails, err := getter(ctx)
		if err != nil {
			st.Log.Error("error getting emails", zap.Int("interatio", i), zap.Error(err))
			continue
		}
		return emails, nil
	}
	return nil, errors.New("error getting emails")
}
