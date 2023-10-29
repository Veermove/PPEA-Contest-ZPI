-- name: GetAssessorsAndRatingsForSubmission :many
select
    base.first_name       as "first_name",
    base.last_name        as "last_name",
    assessor.assessor_id  as "assessor_id",
    full_rating.rating_id as "rating_id",
    full_rating.type      as "rating_type"
from project.assessor_submission
inner join person.assessor    as assessor    on assessor.assessor_id = assessor_submission.assessor_id -- join to get ipma_expert_id
inner join person.ipma_expert as ipma_expert on ipma_expert.ipma_expert_id = assessor.ipma_expert_id   -- join to get person_id
inner join person.base        as base        on base.person_id = ipma_expert.person_id                 -- join to get first_name and last_name
inner join project.rating     as full_rating on full_rating.assessor_id = assessor.assessor_id
where assessor_submission.submission_id = $1
    and full_rating.submission_id = $1;
