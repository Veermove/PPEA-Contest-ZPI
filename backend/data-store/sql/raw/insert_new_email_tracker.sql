-- name: GetEmailTracker :one
insert into emails.sent_for_one_rating (
    assessor_id,
    submission_id,
    rating_type
) values (
    $1,
    $2,
    $3
)
on conflict (assessor_id, submission_id, rating_type) -- this update does nothing.
    do update set rating_type = excluded.rating_type  -- it's here just to make sure that the row returned.
returning *;
