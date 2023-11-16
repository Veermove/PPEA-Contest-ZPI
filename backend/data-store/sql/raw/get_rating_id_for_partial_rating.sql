-- name: GetRatingIdForPartialRating :one
select
    rating.rating_id   as "rating_id"
from project.rating as rating
inner join project.partial_rating as part_rating on part_rating.rating_id = rating.rating_id
where
    part_rating.partial_rating_id = $1;
