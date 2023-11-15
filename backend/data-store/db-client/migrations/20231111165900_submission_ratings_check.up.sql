create schema validation_checks;

create or replace
function
    validation_checks.rating_matches(rating_id_arg integer) returns boolean
as $$
declare
    does_match boolean;
begin
    select
        case
            when rating.type = 'individual' then
                submission.status = 'accepted'

            when rating.type = 'initial' then
                submission.status = 'accepted_individual'

            when rating.type = 'final' then
                submission.status = 'accepted_initial'
        end
    into does_match
    from project.rating as rating
    inner join project.submission as submission on rating.submission_id = submission.submission_id
    where rating.rating_id = rating_id_arg;

    return does_match;
end;
$$ language plpgsql;


alter table only project.partial_rating
    add constraint partial_rating_correct_status
    check (validation_checks.rating_matches(rating_id));

create or replace
function
    validation_checks.all_assessors_submitted_rating(submission_id integer) returns boolean
as $$
begin
    return (
        with assessors_cnt as (
            select count(assessor_submission.assessor_id) as "count"
                from project.assessor_submission as assessor_submission
                where assessor_submission.rating_id = submission_id
        ),
        submitted_individual_ratings_cnt as (
            select count(rating.rating_id) as "count"
                from project.rating as rating
                where rating.submission_id = submission_id
                and rating.is_draft = false
                and rating.type = 'individual'
        )
        select assessors_cnt.count = submitted_individual_ratings_cnt.count as "all_submitted"
            from assessors_cnt, submitted_individual_ratings_cnt
    );
end;
$$ language plpgsql;

create or replace
function
    validation_checks.exactly_one_final_rating(submission_id integer) returns boolean
as $$
begin
    return (
        select count(rating.rating_id) = 1
            from project.rating as rating
            where rating.submission_id = submission_id
            and rating.is_draft = false
            and rating.type = 'final'
    );
end;
$$ language plpgsql;

create or replace
function
    validation_checks.exactly_one_initial_rating(submission_id integer) returns boolean
as $$
begin
    return (
        select count(rating.rating_id) = 1
            from project.rating as rating
            where rating.submission_id = submission_id
            and rating.is_draft = false
            and rating.type = 'initial'
    );
end;
$$ language plpgsql;

create or replace
function
    validation_checks.get_matching_state(current_status project.state, sub_id integer) returns project.state
as $$
declare
    new_status project.state;
begin

    select (
        case
            when current_status = 'accepted' then
                case
                    when validation_checks.all_assessors_submitted_rating(sub_id) then
                        'accepted_individual'
                    else null
                end

            when current_status = 'accepted_individual' then
                case
                    when validation_checks.exactly_one_initial_rating(sub_id) then
                        'accepted_initial'
                    else null
                end
            when current_status = 'accepted_initial' then
                case
                    when validation_checks.exactly_one_final_rating(sub_id) then
                        'accepted_final'
                    else null
                end
            else null
        end
    ) as "new_status" into new_status;

    if new_status is null then
        raise exception 'Invalid submission status';
    end if;

    return new_status;
end;
$$ language plpgsql;


