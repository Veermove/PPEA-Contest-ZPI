-- name: GetFinalRatingsEmails :many
with
asors_for_submis as (
    select
        assessor_submission.assessor_id        as assessor_id,
        submission.submission_id               as submission_id,
        submission.status                      as submission_status,
        submission.name                        as submission_name,
        contest.est_time_individual_assessment as set_before_date
    from
        project.assessor_submission as assessor_submission
        inner join project.submission as submission
            on assessor_submission.submission_id = submission.submission_id
        inner join edition.contest as contest
            on submission.contest_id = contest.contest_id
    where
        submission.status = 'accepted_initial'
        and
        assessor_submission.is_leading = true
),
asors_for_submis_wo_rating as (
    select
        asors_for_submis.assessor_id,
        asors_for_submis.submission_id,
        asors_for_submis.submission_status,
        asors_for_submis.submission_name,
        asors_for_submis.set_before_date,
        rating.rating_id,
        rating.custom_est_assessment_time
    from
        asors_for_submis
        left join project.rating as rating
            on asors_for_submis.assessor_id = rating.assessor_id
            and asors_for_submis.submission_id = rating.submission_id
    where
        rating.rating_id is null
        or (
            rating.type = 'final'
            and
            rating.is_draft = true
        )
)
select
    asors_for_submis_wo_rating.submission_status,
    asors_for_submis_wo_rating.submission_name,
    asors_for_submis_wo_rating.rating_id,
    asors_for_submis_wo_rating.set_before_date,
    base.first_name,
    base.last_name,
    base.email
from asors_for_submis_wo_rating
    inner join person.assessor    as assessor
        on assessor.assessor_id = asors_for_submis_wo_rating.assessor_id -- join to get ipma_expert_id

    inner join person.ipma_expert as ipma_expert
        on ipma_expert.ipma_expert_id = assessor.ipma_expert_id                  -- join to get person_id

    inner join person.base        as base
        on base.person_id = ipma_expert.person_id;                               -- join to get first_name and last_name
