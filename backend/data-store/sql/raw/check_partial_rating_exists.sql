-- name: DoesPartialRatingExist :one
select (
    @partial_rating_id::integer in (
        select partial_rating_id from project.partial_rating
    )
)::boolean;
