-- name: GetDelayedRatings :many
select
    rating.rating_id
from project.rating as rating
inner join project.submission as submission using (submission_id)
inner join edition.contest    as contest    using (contest_id)
where
    rating.is_draft = true
    and
    coalesce(
        rating.custom_est_assessment_time,
        case rating.type
            when 'individual' then contest.est_time_individual_assessment
            when 'initial'    then contest.est_time_initial_assessment
            when 'final'      then contest.est_time_final_assessment
            else '9999-12-31'::date -- should never happen
        end
    ) < now();
