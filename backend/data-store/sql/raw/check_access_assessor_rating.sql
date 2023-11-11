-- name: DoesAssessorHaveAccessToRating :one
select (
    @rating_id::integer in (
        select rating_id
            from project.rating as rating
            inner join project.assessor_submission as assessor_submission
                on assessor_submission.submission_id = rating.submission_id
            where assessor_submission.assessor_id = $1
    )
)::boolean;
