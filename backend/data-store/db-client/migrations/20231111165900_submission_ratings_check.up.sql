create schema validation_checks;

create or replace
function
    validation_checks.rating_matches(rating_id_arg integer) returns boolean
as $$
begin
    return
        select
            case
                when rating.type = 'individual' then (
                    select submission.status = 'accepted' as "correct_status"
                        from project.submission as submission
                        where submission.submission_id = rating.submission_id
                )
                when rating.type = 'initial'    then (
                    select submission.status = 'accepted_individual' as "correct_status"
                        from project.submission as submission
                        where submission.submission_id = rating.submission_id
                )
                when rating.type = 'final'      then (
                    select submission.status = 'accepted_initial' as "correct_status"
                        from project.submission as submission
                        where submission.submission_id = rating.submission_id
                )
                end as "does_match"
        from project.rating as rating
        where rating.rating_id = rating_id_arg;
end;
$$ language plpgsql;

alter table only project.partial_rating
    add constraint partial_rating_correct_status
    check (validation_checks.rating_matches(rating_id));
