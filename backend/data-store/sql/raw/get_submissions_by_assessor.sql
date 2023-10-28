-- name: GetSubmissionsByAssessorEmail :many
select
    submission.submission_id as "submission_id",
    contest.year             as "year",
    submission.name          as "name",
    submission.duration_days as "duration_days"
from project.assessor_submission assessor_submission
inner join project.submission submission on submission.submission_id = assessor_submission.submission_id
inner join edition.contest    contest    on contest.contest_id       = submission.contest_id
inner join person.base        person     on person.person_id         = submission.person_id
where
    person.email = $1;

