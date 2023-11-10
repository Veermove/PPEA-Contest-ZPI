-- name: SubmitRating :one
update project.rating set (
    "is_draft"
) = (
    $2
)
where rating_id = $1
returning *;


