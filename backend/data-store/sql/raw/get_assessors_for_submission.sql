-- name: GetAssessorsForSubmission :many
select
    base.first_name      as "first_name",
    base.last_name       as "last_name",
    assessor.assessor_id as "assessor_id"
from project.assessor_submission
inner join person.assessor    as assessor    on assessor.assessor_id = assessor_submission.assessor_id -- join to get ipma_expert_id
inner join person.ipma_expert as ipma_expert on ipma_expert.ipma_expert_id = assessor.ipma_expert_id   -- join to get person_id
inner join person.base        as base        on base.person_id = ipma_expert.person_id                 -- join to get first_name and last_name
where assessor_submission.submission_id = $1;
