-- name: DoesAssessorHaveAccess :one

select (
    @submission_id::integer in (
        select submission_id
            from project.assessor_submission
            where assessor_id = $1
    )
)::boolean;
