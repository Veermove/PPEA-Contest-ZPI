create schema emails;


-- we need to keep track how many emails were sent for each rating
-- we don't want to send more than two emails for each rating to each assessor
-- one email is sent 5 days before deadline
-- second email is sent 1 day before deadline
create table emails.sent_for_one_rating (

    -- we can identify "email instance" by these three columns
    "assessor_id"             int                 not null,
    "submission_id"           int                 not null,
    "rating_type"             project.rating_type not null,

    "emails_sent" int not null default 0,

    constraint assessor_submission_assessor_fk
        foreign key (assessor_id) references person.assessor(assessor_id),

    constraint assessor_submission_submission_fk
        foreign key (submission_id) references project.submission(submission_id),

    constraint sent_for_one_rating_pk
        primary key (assessor_id, submission_id, rating_type)
);

create or replace function emails.delete_tracker()
returns trigger as $$
begin
    if new.is_draft is true then
        return old;
    end if;


    delete from emails.sent_for_one_rating
    where
        assessor_id = old.assessor_id
        and
        submission_id = old.submission_id
        and
        rating_type = old.type;
    return old;
end;
$$ language plpgsql;


create trigger delete_emails_tracker_trigger
    after update on project.rating
    for each row
    execute function emails.delete_tracker();
