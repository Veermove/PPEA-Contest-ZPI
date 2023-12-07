-- name: CheckEmailTrackerExist :one
select exists(
    select *
    from emails.sent_for_one_rating
    where
        assessor_id = $1
        and
        submission_id = $2
        and
        rating_type = $3
) as exists;

