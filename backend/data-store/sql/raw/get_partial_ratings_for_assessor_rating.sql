-- name: GetPRatingsForAssessorAndRatingID :many
select
    rating.partial_rating_id,
    rating.criterion_id,
    rating.points,
    rating.justification,
    rating.modified,
    rating.modified_by_id
from project.rating as full_rating
inner join project.partial_rating as rating on full_rating.rating_id = rating.rating_id
where full_rating.rating_id = $1
    and full_rating.assessor_id = $2;
