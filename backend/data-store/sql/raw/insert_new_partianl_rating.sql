-- name: CreatePartialRating :one
insert into project.partial_rating (
    "rating_id",
    "criterion_id",

    "points",
    "justification",

    "modified",
    "modified_by_id"
) values (
    $1,
    $2,
    $3,
    $4,
    current_timestamp,
    $5
) returning *;


