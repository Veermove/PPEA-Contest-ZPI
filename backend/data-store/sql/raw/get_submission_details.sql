-- name: GetSubmissionDetails :one
select
    submission.team_size,
    submission.finish_date,
    submission.status,
    submission.budget::text,
    submission.description,
    report.is_draft,
    report.report_submission_date,
    report.project_goals,
    report.organisation_structure,
    report.division_of_work,
    report.project_schedule,
    report.attatchments
from project.submission as submission
inner join project.application_report as report on report.submission_id = submission.submission_id
where submission.submission_id = $1;
