-- name: IncrementEmailTracker :exec
update emails.sent_for_one_rating
set
    emails_sent = emails_sent + 1
where
    assessor_id = $1
    and
    submission_id = $2
    and
    rating_type = $3
returning *;
