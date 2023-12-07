-- name: DoesPartialRatingExist :one
select exists(
    select *
    from project.partial_rating
    where
        rating_id = $1 and criterion_id = $2
) as "exists";
