-- name: SubmitRating :one
update project.rating set
    "is_draft" = @is_draft
where rating_id = @rating_id
returning *;


