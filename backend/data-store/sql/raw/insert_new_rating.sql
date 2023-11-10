-- name: CreateProjectRating :one
insert into project.rating (
    "submission_id",
    "assessor_id",
    "is_draft",
    "type"
) values (
    $1,
    (
        select assessor_id from project.assessor_submission
            where is_leading = true and submission_id = $1
    ),
    $2,
    $3
) returning *;


