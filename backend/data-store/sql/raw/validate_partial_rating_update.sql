-- name: ValidatePartialRating :one

with get_modified_ts as (
    select
        modified at time zone 'utc' as "modified"
    from project.partial_rating
    where partial_rating_id = @partial_rating_id
),
eq_timestamps as (
    select date_trunc('milliseconds', @modified::timestamp) = date_trunc('milliseconds', (select modified from get_modified_ts)) as "eq_timestamps"
)
select (
    case
        when @partial_rating_id::integer in (
            select partial_rating_id from project.partial_rating
        ) then
            case
                when (select eq_timestamps from eq_timestamps) then ''::text
                else (select to_char(modified, 'YYYY-MM-DD"T"HH24:MI:SS.USZ') from get_modified_ts)::text
            end
        else 'id'::text
    end
)::text;
