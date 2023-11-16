-- name: SetNextStatus :exec
update project.submission as submission set
    "status" = validation_checks.get_matching_state(submission.status, submission.submission_id)
where submission_id = @submission_id;


