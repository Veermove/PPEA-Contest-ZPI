package dbclient

import (
	"context"
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
		SetBeforeDate  time.Time
		IsCreated      bool
		FirstName      string
		LastName       string
		Email          string

		RatingType    ds.RatingType
		RatingSqlType queries.ProjectRatingType
	}
)

func (st *Store) ConfirmEmailsSent(ctx context.Context, confirmations []*ds.Confirmation) error {

	// start transaction
	tx, err := st.Pool.Begin(ctx)

	// if we commit transaction before the end this becomes a no-op
	defer tx.Rollback(ctx)

	if err != nil {
		return fmt.Errorf("starting transaction: %w", err)
	}

	dbConn := queries.New(tx)

	for _, req := range confirmations {

		log := st.Log.With(
			zap.Int32("assessor_id", req.GetAssessorId()),
			zap.Int32("submission_id", req.GetSubmissionId()),
			zap.String("rating_type", req.GetRatingType().String()))

		exists, err := dbConn.CheckEmailTrackerExist(ctx, queries.CheckEmailTrackerExistParams{
			AssessorID:   req.GetAssessorId(),
			SubmissionID: req.GetSubmissionId(),
			RatingType:   RatingTypesMappingRev[req.GetRatingType()],
		})

		if err != nil {
			log.Info("exiting email confirmation. Error checking email tracker", zap.Error(err))
			return fmt.Errorf("checking email tracker: %w", err)
		}
		if !exists {
			log.Info("exiting email confirmation. Email tracker does not exist")
			return fmt.Errorf("email tracker does not exist")
		}

		if err := dbConn.IncrementEmailTracker(ctx, queries.IncrementEmailTrackerParams{
			AssessorID:   req.GetAssessorId(),
			SubmissionID: req.GetSubmissionId(),
			RatingType:   RatingTypesMappingRev[req.GetRatingType()],
		}); err != nil {

			log.Error("error incrementing email tracker", zap.Error(err))
			return fmt.Errorf("incrementing email tracker: %w", err)
		}
	}

	st.Log.Info("committing transaction with email confirmations")

	return tx.Commit(ctx)
}

func (st *Store) GetEmailDetails(ct context.Context) (*ds.EmailResponse, error) {
	var (
		ctx, cancel = context.WithTimeout(ct, 30*time.Minute)
		wgSink      = &sync.WaitGroup{}
		wgChan      = &sync.WaitGroup{}

		combinedRatingsDetails = make(chan *EmailRow)
		merged                 = []*EmailRow{}
	)
	defer cancel()

	st.Log.Info("preparing to get emails details")

	wgSink.Add(1)
	go func() {
		defer wgSink.Done()

		for email := range combinedRatingsDetails {

			select { // This is to prevent hanging goroutine
			case <-ctx.Done(): // when context get cancelled
				return
			default:
			}
			merged = append(merged, email)
		}
	}()

	wgChan.Add(3)
	go st.getEmails(ctx, wgChan, combinedRatingsDetails, st.getEmailsForIndividualRatings)
	go st.getEmails(ctx, wgChan, combinedRatingsDetails, st.getEmailsForFinalRatings)
	go st.getEmails(ctx, wgChan, combinedRatingsDetails, st.getEmailsForInitialRatings)

	wgChan.Wait()
	close(combinedRatingsDetails)
	wgSink.Wait()

	emailsToSend := make([]*ds.EmailDetails, 0, len(merged))
	dbConn := queries.New(st.Pool)

	st.Log.Info("got emails details", zap.Int("emails", len(merged)), zap.Time("now", time.Now().UTC()))

	for _, email := range merged {

		if !email.SetBeforeDate.IsZero() { // Unreachable if I'm sane
			st.Log.Error("email has no set before date", zap.Any("email", email))
			continue
		}

		var ( // Are we ...

			pastWarnings = time.Now().UTC().After(email.SetBeforeDate)

			// Let's say:
			// today               = 13.05
			// deadline date       = 15.05
			// then:
			// warningPeriodStart = deadline - EmailWarningPeriod (5 days) = 10.05
			// is warningPeriodStart (10.05) before now (13.05)?
			//    - yes, so we are in EmailWarningPeriod
			//    - no,  so we are not in EmailWarningPeriod
			inWarningPeriod      = email.SetBeforeDate.UTC().Add(-EmailWarningPeriod).Before(time.Now().UTC())
			inFinalWarningPeriod = email.SetBeforeDate.UTC().Add(-EmailWarningFinalPeriod).Before(time.Now().UTC())

			log = st.Log.With(
				zap.String("email", email.Email),
				zap.String("rating_type", email.RatingType.String()),
				zap.String("rating_submit_date", email.SetBeforeDate.UTC().Format(time.RFC3339)),
				zap.String("submission_name", email.SubmissionName))
		)

		if pastWarnings || !inWarningPeriod {
			log.Debug("skipping email", zap.Bool("past_warnings", pastWarnings), zap.Bool("in_warning_period", inWarningPeriod))
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
			log.Error("error getting email tracker", zap.Error(err))
			continue
		}

		if trackerp == nil { // Unreachable if I'm sane
			log.Error("no email tracker found", zap.Any("email", email))
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
			RatingSubmitDate:  email.SetBeforeDate.UTC().Format(time.RFC3339),
			AssessorId:        email.AssessorID,
			SubmissionId:      email.SubmissionID,
		}

		// Sending first mail
		if tracker.EmailsSent == 0 && inWarningPeriod {
			log.Info("sending first warning")
			emailDetails.IsFirstWarning = true
			emailsToSend = append(emailsToSend, emailDetails)
			continue
		}

		// Sending second mail
		if tracker.EmailsSent == 1 && inFinalWarningPeriod {
			log.Info("sending second warning")
			emailDetails.IsFirstWarning = false
			emailsToSend = append(emailsToSend, emailDetails)
			continue
		}

		log.Info("skipping email", zap.Int32("emails_sent", tracker.EmailsSent))
	}

	return &ds.EmailResponse{
		Emails: emailsToSend,
	}, nil
}

func (st *Store) getEmails(ctx context.Context,
	wg *sync.WaitGroup,
	combinedRatingsDetails chan<- *EmailRow,
	getter func(context.Context) ([]*EmailRow, error),
) {
	defer wg.Done()

	emails, err := getWithBackoff(ctx, st.Log, 3, getter)
	if err != nil {
		st.Log.Error("error getting emails", zap.Error(err))
		return
	}
	for _, email := range *emails {
		combinedRatingsDetails <- email
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
