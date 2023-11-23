-- name: GetSubmissionQuestions :many
select
    jury_question_id,
    question
from project.jury_question as jury_question
where
    jury_question.submission_id = $1
    and jury_question.is_draft = false;
