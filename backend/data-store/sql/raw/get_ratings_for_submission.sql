-- name: GetRatingsForSubission :many
select
    rating.rating_id   as "rating_id",
    rating.type        as "type",
    rating.is_draft    as "is_draft",
    rating.assessor_id as "assessor_id"
from project.rating as rating
where
    rating.submission_id = $1;
