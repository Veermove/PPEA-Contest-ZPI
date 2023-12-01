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

		RatingType    ds.RatingType
		RatingSqlType queries.ProjectRatingType
	}
)

func (st *Store) GetEmailDetails(ct context.Context) (*ds.EmailResponse, error) {
	var (
		ctx, cancel       = context.WithTimeout(ct, 30*time.Minute)
		wg                = sync.WaitGroup{}
		individualRatings = []*EmailRow{}
		finalRatings      = []*EmailRow{}
		initialRatings    = []*EmailRow{}
	)
	defer cancel()
	wg.Add(3)

	go func() {
		defer wg.Done()
		emails, err := getWithBackoff(ctx, st.Log, 3, st.getEmailsForFinalRatings)
		if err != nil {
			st.Log.Error("error getting emails", zap.Error(err))
			return
		}
		finalRatings = *emails
	}()

	go func() {
		defer wg.Done()
		emails, err := getWithBackoff(ctx, st.Log, 3, st.getEmailsForIndividualRatings)
		if err != nil {
			st.Log.Error("error getting emails", zap.Error(err))
			return
		}
		individualRatings = *emails
	}()

	go func() {
		defer wg.Done()
		emails, err := getWithBackoff(ctx, st.Log, 3, st.getEmailsForInitialRatings)
		if err != nil {
			st.Log.Error("error getting emails", zap.Error(err))
			return
		}
		individualRatings = *emails
	}()

	wg.Wait()

	merged := append(append(individualRatings, finalRatings...), initialRatings...)
	emailsToSend := make([]*ds.EmailDetails, 0, len(merged))
	dbConn := queries.New(st.Pool)

	for _, email := range merged {

		if !email.SetBeforeDate.Valid { // Unreachable if I'm sane
			st.Log.Error("email has no set before date", zap.Any("email", email))
			continue
		}

		var ( // Are we ...

			pastWarnings = time.Now().After(email.SetBeforeDate.Time)

			// Let's say:
			// today               = 13.05
			// deadline date       = 15.05
			// then:
			// warningPeriodStart = deadline - EmailWarningPeriod (5 days) = 10.05
			// is warningPeriodStart (10.05) after now (13.05)?
			//    - yes, so we are in EmailWarningPeriod
			//    - no,  so we are not in EmailWarningPeriod
			inWarningPeriod      = email.SetBeforeDate.Time.Add(-EmailWarningPeriod).After(time.Now())
			inFinalWarningPeriod = email.SetBeforeDate.Time.Add(-EmailWarningFinalPeriod).After(time.Now())
		)

		if pastWarnings || !inWarningPeriod {
			continue
		}

		// From this point we know that we are in EmailWarningPeriod.
		// Get email tracker
		trackerp, err := getWithBackoff(ctx, st.Log, 3, func(ctx context.Context) (queries.EmailsSentForOneRating, error) {
			return dbConn.GetEmailTracker(ctx, queries.GetEmailTrackerParams{
				AssessorID:   email.AssessorID,
				SubmissionID: email.SubmissionID,
				RatingType:   email.RatingSqlType,
			})
		})

		if err != nil {
			st.Log.Error("error getting email tracker", zap.Error(err))
			continue
		}

		if trackerp == nil { // Unreachable if I'm sane
			st.Log.Error("no email tracker found", zap.Any("email", email))
			continue
		}

		tracker := *trackerp
		emailDetails := &ds.EmailDetails{
			AssessorFirstName: email.FirstName,
			AssessorLastName:  email.LastName,
			AssessorEmail:     email.Email,
			SubmissionName:    email.SubmissionName,
			IsRatingCreated:   email.IsCreated,
			RatingType:        email.RatingType,
			EditionYear:       email.Year,
			RatingSubmitDate:  email.SetBeforeDate.Time.UTC().Format(time.RFC3339),
		}

		// Sending first mail
		if tracker.EmailsSent == 0 && inWarningPeriod {
			emailDetails.IsFirstWarning = true

			if err := dbConn.IncrementEmailTracker(ctx, queries.IncrementEmailTrackerParams{
				AssessorID:   email.AssessorID,
				SubmissionID: email.SubmissionID,
				RatingType:   email.RatingSqlType,
			}); err != nil {
				st.Log.Error("error incrementing email tracker", zap.Error(err))
				continue
			}

			emailsToSend = append(emailsToSend, emailDetails)
			continue
		}

		// Sending second mail
		if tracker.EmailsSent == 1 && inFinalWarningPeriod {
			emailDetails.IsFirstWarning = false

			if err := dbConn.IncrementEmailTracker(ctx, queries.IncrementEmailTrackerParams{
				AssessorID:   email.AssessorID,
				SubmissionID: email.SubmissionID,
				RatingType:   email.RatingSqlType,
			}); err != nil {
				st.Log.Error("error incrementing email tracker", zap.Error(err))
				continue
			}

			emailsToSend = append(emailsToSend, emailDetails)
			continue
		}
	}

	return &ds.EmailResponse{
		Emails: emailsToSend,
	}, nil
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
			RatingType:     ds.RatingType_INDIVIDUAL,
			RatingSqlType:  queries.ProjectRatingTypeIndividual,
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
			RatingType:     ds.RatingType_FINAL,
			RatingSqlType:  queries.ProjectRatingTypeFinal,
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
			RatingType:     ds.RatingType_INITIAL,
			RatingSqlType:  queries.ProjectRatingTypeInitial,
		}
	}), nil
}

func getWithBackoff[T any](
	ctx context.Context,
	logger *zap.Logger,
	backoff int,
	getter func(context.Context) (T, error),
) (*T, error) {

	for i := 0; i < backoff; i++ {
		emails, err := getter(ctx)
		if err != nil {
			logger.Error("error getting emails", zap.Int("interatio", i), zap.Error(err))
			continue
		}
		return &emails, nil
	}
	return nil, errors.New("error getting emails")
}
