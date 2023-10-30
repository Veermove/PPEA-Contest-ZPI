-- name: GetCriteriaForSubmission :many
select
    criteria.pem_criterion_id,
    criteria.name,
    criteria.description,
    criteria.area,
    criteria.criteria,
    criteria.subcriteria
from project.submission as proj_submission
inner join edition.contest as contest on proj_submission.contest_id = contest.contest_id
inner join edition.pem_criterion as criteria on contest.contest_id = criteria.contest_id
where proj_submission.submission_id = $1;
