package dbclient

import (
	"context"
	"fmt"
	"strings"
	"time"
	pb "zpi/pb"
	queries "zpi/sql/gen"

	"github.com/jackc/pgx/v4"
	"github.com/samber/lo"
	"github.com/sourcegraph/conc/iter"
	"github.com/sourcegraph/conc/pool"
	"go.uber.org/zap"
)

// GetUserClaims returns all ids as other roles that the user has
func (st *Store) GetUserClaims(ctx context.Context, email string) (*pb.UserClaimsResponse, error) {
	usr, err := queries.New(st.Pool).GetUserClaims(ctx, email)
	if err == pgx.ErrNoRows {
		return nil, fmt.Errorf("User not found")
	} else if err != nil {
		return nil, fmt.Errorf("getting user claims: %w", err)
	}

	return &pb.UserClaimsResponse{
		FirstName:              usr.FirstName,
		LastName:               usr.LastName,
		PersonId:               usr.PersonID,
		AssessorId:             DenullifyInt32(usr.AssessorID),
		AwardsRepresentativeId: DenullifyInt32(usr.AwardsRepresentativeID),
		JuryMemberId:           DenullifyInt32(usr.JuryMemberID),
		IpmaExpertId:           DenullifyInt32(usr.IpmaExpertID),
		ApplicantId:            DenullifyInt32(usr.ApplicantID),
	}, nil

}

// GetSubmissionsDetails returns detailed information about submission with given id if the user has access to it.
func (st *Store) GetSubmissionDetails(ctx context.Context, submissionId, assessorId int32) (*pb.DetailsSubmissionResponse, error) {
	hasAccess, err := queries.New(st.Pool).DoesAssessorHaveAccess(ctx, AccessParams{AssessorID: assessorId, SubmissionID: submissionId})
	if err != nil {
		return nil, fmt.Errorf("checking access: %w", err)
	}
	if !hasAccess {
		return nil, fmt.Errorf("User does not have access to resource")
	}

	details, err := queries.New(st.Pool).GetSubmissionDetails(ctx, submissionId)
	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("getting submission details: %w", err)
	}

	var submissionDate string
	if details.ReportSubmissionDate.Valid && details.ReportSubmissionDate.Time != (time.Time{}) {
		submissionDate = details.ReportSubmissionDate.Time.Format(time.RFC3339)
	} else {
		submissionDate = ""
	}

	return &pb.DetailsSubmissionResponse{
		TeamSize:    details.TeamSize,
		FinishDate:  details.FinishDate.Format(time.RFC3339),
		Status:      SubmissionStatesTypesMapping[details.Status],
		Budget:      details.SubmissionBudget,
		Description: details.Description,
		Report: &pb.AppReport{
			IsDraft:               details.IsDraft,
			ReportSubmissionDate:  submissionDate,
			ProjectGoals:          DenullifyStr(details.ProjectGoals),
			OrganisationStructure: DenullifyStr(details.OrganisationStructure),
			DivisionOfWork:        DenullifyStr(details.DivisionOfWork),
			ProjectSchedule:       DenullifyStr(details.ProjectSchedule),
			Attachments:           DenullifyStr(details.Attatchments),
		},
	}, nil
}

// GetSubmissionsByAssessor returns all submissions for the given assessor
func (st *Store) GetSubmissionsByAssessor(ctx context.Context, assessorId int32) (*pb.SubmissionsResponse, error) {
	returnVal := &pb.SubmissionsResponse{Submissions: []*pb.Submission{}}

	subs, err := queries.New(st.Pool).GetSubmissionsByAssessorId(ctx, assessorId)

	if err == pgx.ErrNoRows {
		return returnVal, nil
	}
	if err != nil {
		return nil, fmt.Errorf("getting submissions by assessor id: %w", err)
	}

	submissions, err := iter.MapErr(subs, func(subm *queries.GetSubmissionsByAssessorIdRow) (*pb.Submission, error) {
		submission := &pb.Submission{
			SubmissionId: subm.SubmissionID,
			Year:         subm.Year,
			Name:         subm.Name,
			DurationDays: subm.DurationDays,
		}

		assessors, errAs := queries.New(st.Pool).GetAssessorsForSubmission(ctx, subm.SubmissionID)

		if errAs != nil && errAs != pgx.ErrNoRows {
			return nil, fmt.Errorf("getting assessors for submission: %w", errAs)
		}

		ratings, errRt := queries.New(st.Pool).GetRatingsForSubission(ctx, subm.SubmissionID)

		if errRt != nil && errRt != pgx.ErrNoRows {
			return nil, fmt.Errorf("getting ratings for submission: %w", errRt)
		}

		submission.Assessors = MapAssessorsFromSql(assessors)
		submission.Ratings = MapRatingsFromSql(ratings)
		return submission, nil
	})

	st.Log.Debug("GetSubmissionsByAssessor", zap.Int("submissions", len(submissions)))

	if err != nil {
		return nil, err
	}
	return &pb.SubmissionsResponse{Submissions: submissions}, nil
}

func (st *Store) GetSubmissionRatings(ctx context.Context, submissionId, assessorId int32) (*pb.RatingsSubmissionResponse, error) {

	hasAccess, err := queries.New(st.Pool).DoesAssessorHaveAccess(ctx, AccessParams{AssessorID: assessorId, SubmissionID: submissionId})
	if err != nil {
		return nil, fmt.Errorf("checking access: %w", err)
	}
	if !hasAccess {
		return nil, fmt.Errorf("User does not have access to resource")
	}

	// Run query to get slice of
	// * AssessorID
	// * RatingID
	// * RatingType
	assessorsWithRating, err := queries.New(st.Pool).GetAssessorsAndRatingsForSubmission(ctx, submissionId)

	if err != nil && err != pgx.ErrNoRows {
		return nil, fmt.Errorf("getting assessors and ratings for submission: %w", err)
	}

	var (
		// Group by rating type ==> map[RatingType]        ->  []struct{ AssessorID, RatingID }
		// in golang types      ==> map[ProjectRatingType] ->  []*GetAssessorsAndRatingsForSubmissionRow
		ratingsByType = lo.GroupBy(
			lo.Map(assessorsWithRating, func(a queries.GetAssessorsAndRatingsForSubmissionRow, _ int) *queries.GetAssessorsAndRatingsForSubmissionRow {
				return &a
			}),
			func(a *queries.GetAssessorsAndRatingsForSubmissionRow) queries.ProjectRatingType {
				return a.RatingType
			},
		)

		// return value
		returnVal = &pb.RatingsSubmissionResponse{}

		// pool of workers for the rest of logic
		workers = pool.New().
			WithContext(ctx).
			WithMaxGoroutines(4).
			WithCancelOnError().
			WithFirstError()

		invidualRatingsHandler = func(ctx context.Context) error {
			individual, _ := ratingsByType[queries.ProjectRatingTypeIndividual]
			individualRs := []*pb.AssessorRatings{}

			// For each struct{ AssessorID, RatingID } ...
			for _, i := range individual {

				ast, err := st.CreateAssessorRatings(ctx,
					i.RatingID,
					i.AssessorID,
					i.FirstName,
					i.LastName,
					i.IsDraft,
				)
				if err != nil && err != pgx.ErrNoRows {
					return fmt.Errorf("creating individual rating for rating %d, assessor %d: %w", i.RatingID, i.AssessorID, err)
				}

				// ... and append to individualRs.
				individualRs = append(individualRs, ast)

			}

			returnVal.Individual = individualRs
			return nil
		}

		initRatingsHandler = func(ctx context.Context) error {

			initials, _ := ratingsByType[queries.ProjectRatingTypeInitial]

			// Generally we expect only one initial rating per submission
			if len(initials) > 1 {
				return fmt.Errorf("more than one initial rating for submission %d", submissionId)

			} else if len(initials) > 0 {
				// Get initial rating
				initial, err := st.CreateAssessorRatings(ctx,
					initials[0].RatingID,
					initials[0].AssessorID,
					initials[0].FirstName,
					initials[0].LastName,
					initials[0].IsDraft,
				)

				if err != nil && err != pgx.ErrNoRows {
					return fmt.Errorf("creating initial rating for rating %d, assessor %d: %w", initials[0].RatingID, initials[0].AssessorID, err)
				}

				returnVal.Initial = initial
			}
			return nil
		}

		finalRratingsHandler = func(ctx context.Context) error {
			finals, _ := ratingsByType[queries.ProjectRatingTypeFinal]

			// Generally we expect only one final rating per submission
			if len(finals) > 1 {
				return fmt.Errorf("more than one final rating for submission %d", submissionId)

			} else if len(finals) > 0 {
				// Get final rating
				final, err := st.CreateAssessorRatings(ctx,
					finals[0].RatingID,
					finals[0].AssessorID,
					finals[0].FirstName,
					finals[0].LastName,
					finals[0].IsDraft,
				)

				if err != nil {
					return fmt.Errorf("creating final rating for rating %d, assessor %d: %w", finals[0].RatingID, finals[0].AssessorID, err)
				}

				returnVal.Final = final
			}
			return nil
		}

		criteriaHandler = func(ctx context.Context) error {
			critsRaw, err := queries.New(st.Pool).GetCriteriaForSubmission(ctx, submissionId)
			if err != nil && err != pgx.ErrNoRows {
				return fmt.Errorf("getting criteria for submission: %w", err)
			}

			crits := []*pb.Criterion{}
			for _, c := range critsRaw {
				crits = append(crits, &pb.Criterion{
					CriterionId: c.PemCriterionID,
					Name:        c.Name,
					Description: c.Description,
					Area:        c.Area,
					Criteria:    c.Criteria,
					Subcriteria: DenullifyStr(c.Subcriteria),
				})
			}

			returnVal.Criteria = crits
			return nil
		}
	)

	// Handle individual ratings
	workers.Go(invidualRatingsHandler)

	// Handle initial ratings
	workers.Go(initRatingsHandler)

	// Handle final ratings
	workers.Go(finalRratingsHandler)

	// Fetch criteria
	workers.Go(criteriaHandler)

	if err := workers.Wait(); err != nil {
		return nil, err
	}

	return returnVal, nil
}

func (st *Store) GetStudyVisits(ctx context.Context, assessorId, submissionId int32) (*pb.StudyVisitResponse, error) {
	hasAccess, err := queries.New(st.Pool).DoesAssessorHaveAccess(ctx, AccessParams{AssessorID: assessorId, SubmissionID: submissionId})
	if err != nil {
		return nil, fmt.Errorf("checking access: %w", err)
	}
	if !hasAccess {
		return nil, fmt.Errorf("User does not have access to resource")
	}

	db := queries.New(st.Pool)

	// Get all questions for the submission
	visits, err := db.GetSubmissionQuestions(ctx, submissionId)
	if err != nil && err != pgx.ErrNoRows {
		return nil, fmt.Errorf("getting study visits for submission: %w", err)
	}
	if len(visits) == 0 || err == pgx.ErrNoRows {
		return &pb.StudyVisitResponse{}, nil
	}

	questions := make([]*pb.Question, 0, len(visits))

	for _, row := range visits {
		q := &pb.Question{Content: row.Question}

		// ... for each question get all answers.
		answerRows, err := db.GetAnswersForQuestion(ctx, row.JuryQuestionID)
		if err != nil && err != pgx.ErrNoRows {
			return nil, fmt.Errorf("getting answers for question %d: %w", row.JuryQuestionID, err)
		}

		answers := make([]*pb.Answer, 0, len(answerRows))

		for _, a := range answerRows {

			var urls = []string{}
			if a.Files.Valid && a.Files.String != "" {
				// NOTE files are comma separated list of file locators (ids, urls, names, etc.)
				urls = append(urls, strings.Split(strings.Trim(a.Files.String, " \r\t\n"), ",")...)
			}
			answers = append(answers, &pb.Answer{
				AnswerText: DenullifyStr(a.Answer),
				Files:      urls,
			})
		}
		q.Answers = answers
		questions = append(questions, q)
	}

	return &pb.StudyVisitResponse{Questions: questions}, nil
}
