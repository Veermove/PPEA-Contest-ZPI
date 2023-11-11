-- name: UpdatePartialRating :one
update project.partial_rating set (
    "points",
    "justification",
    "modified",
    "modified_by_id"
) = (
    $2,
    $3,
    current_timestamp,
    $4
)
where partial_rating_id = $1
returning *;


