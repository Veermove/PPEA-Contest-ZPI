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
    validation_checks.all_assessors_submitted_rating(submission_id_arg integer) returns boolean
as $$
begin
    return (
        with assessors_cnt as (
            select count(assessor_submission.assessor_id) as "count"
                from project.assessor_submission as assessor_submission
                where assessor_submission.submission_id = submission_id_arg
        ),
        submitted_individual_ratings_cnt as (
            select count(rating.rating_id) as "count"
                from project.rating as rating
                where rating.submission_id = submission_id_arg
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
    validation_checks.exactly_one_final_rating(submission_id_arg integer) returns boolean
as $$
begin
    return (
        select count(rating.rating_id) = 1
            from project.rating as rating
            where rating.submission_id = submission_id_arg
            and rating.is_draft = false
            and rating.type = 'final'
    );
end;
$$ language plpgsql;

create or replace
function
    validation_checks.exactly_one_initial_rating(submission_id_arg integer) returns boolean
as $$
begin
    return (
        select count(rating.rating_id) = 1
            from project.rating as rating
            where rating.submission_id = submission_id_arg
            and rating.is_draft = false
            and rating.type = 'initial'
    );
end;
$$ language plpgsql;

create or replace
function
    validation_checks.get_next_state(current_status project.state, sub_id integer) returns project.state
as $$
declare
    new_status project.state;
begin

    -- we disallow changing status from rejected
    if current_status = 'rejected' then
        raise exception 'Cannot change status from state % for submission with id %', current_status, sub_id;
    end if;

    -- we disallow changing status from accepted_final
    if current_status = 'accepted_final' then
        raise exception 'Cannot change status from state % for submission with id %', current_status, sub_id;
    end if;

    -- after draft we can only go to submitted
    if current_status = 'draft' then
        return 'submitted';
    end if;

    -- after submitted we can only go to accepted
    if current_status = 'submitted' then
        return 'accepted';
    end if;


    -- for other states we need to validate that...
    if current_status = 'accepted' then

        -- in accepted state we need to validate that all assessors that were assigned
        -- to that submission have submitted their ratings
        if validation_checks.all_assessors_submitted_rating(sub_id) then
            -- if so we go to accepted_individual
            return 'accepted_individual';
        else
            raise exception 'Cannot change status from state % to accepted_individual because not all assessors submitted ratings for submission with id %', current_status, sub_id;
        end if;

    elsif current_status = 'accepted_individual' then

        -- in accepted_individual state we need to validate
        -- that exactly one initial rating has been submitted
        if validation_checks.exactly_one_initial_rating(sub_id) then
            -- if so we go to accepted_initial
            return 'accepted_initial';
        else
            raise exception 'Cannot change status from state % to accepted_initial because not exactly one initial rating was submitted for submission with id %', current_status, sub_id;
        end if;

    elsif current_status = 'accepted_initial' then

        -- in accepted_initial state we need to validate
        -- that exactly one final rating has been submitted
        if validation_checks.exactly_one_final_rating(sub_id) then
            -- if so we go to accepted_final
            return 'accepted_final';
        else
            raise exception 'Cannot change status from state % to accepted_final because not exactly one final rating was submitted for submission with id %', current_status, sub_id;
        end if;
    end if;

    raise exception 'Unreachable fallthrough get_matching_state % %', current_status, sub_id;
end;
$$ language plpgsql;


