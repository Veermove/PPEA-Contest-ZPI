-- name: CreateProjectRatingIndividual :one
insert into project.rating (
    "submission_id",
    "assessor_id",
    "is_draft",
    "type"
) values (
    $1,
    $2,
    $3,
    $4
) returning *;


