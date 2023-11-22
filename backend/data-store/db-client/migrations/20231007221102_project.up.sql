create schema project;
create schema pem;

create type project.state as enum (
    'draft',
    'submitted',
    'accepted',             -- accepted by PEM, waiting for individual ratings
    'accepted_individual',  -- individual ratings submitted, waiting for initial rating
    'accepted_initial',     -- initial rating submitted, waiting for final rating
    'accepted_final',       -- final rating submitted
    'rejected'
);

create type project.rating_type as enum (
    'individual',
    'initial',
    'final'
);

-- raport_aplikacyjny
create table project.application_report (
    "application_report_id"  int     generated always as identity primary key,
    "submission_id"          int     not null,
    "is_draft"               boolean not null,
    "report_submission_date" date,

    "project_goals"          text,
    "organisation_structure" text,
    "division_of_work"       text,
    "project_schedule"       text,

    "attatchments" text
);

create index application_report_submission_id_idx
    on project.application_report(submission_id);

-- zgloszenie_projektu
create table project.submission (
    "submission_id"    int           generated always as identity primary key,
    "contest_id"       int           not null,
    "name"             text          not null,
    "duration_days"    int           not null,
    "team_size"        int           not null,
    "finish_date"      date          not null,
    "status"           project.state not null,
    "budget"           money         not null,
    "description"      text          not null,
    "reference_letter" text,
    "photos"           text,
    "video"            text,
    "logo"             text,

    "initiator_declaration" text,
    "applicant_declaration" text,

    constraint submission_contest_fk
        foreign key (contest_id) references edition.contest(contest_id)
);

alter table only project.application_report
    add constraint application_report_submission_fk
        foreign key (submission_id) references project.submission(submission_id);


-- asesor_zgloszenie_projektu
create table project.assessor_submission (
    "assessor_submission_id" int     generated always as identity primary key,
    "assessor_id"            int     not null,
    "submission_id"          int     not null,
    "is_leading"             boolean not null,

    constraint assessor_submission_assessor_fk
        foreign key (assessor_id) references person.assessor(assessor_id),

    constraint assessor_submission_submission_fk
        foreign key (submission_id) references project.submission(submission_id)
);

create index assessor_submission_assessor_id_idx
    on project.assessor_submission(assessor_id);

create index assessor_submission_submission_id_idx
    on project.assessor_submission(submission_id);


-- aplikant_zgloszenie_projektu
create table project.applicant_submission (
    "applicant_submission_id" int generated always as identity primary key,
    "applicant_id"            int not null,
    "submission_id"           int not null,

    constraint applicant_submission_applicant_fk
        foreign key (applicant_id) references person.applicant(applicant_id),

    constraint applicant_submission_submission_fk
        foreign key (submission_id) references project.submission(submission_id)
);

create index applicant_submission_applicant_id_idx
    on project.applicant_submission(applicant_id);

create index applicant_submission_submission_id_idx
    on project.applicant_submission(submission_id);

-- ocena_PEM
create table project.rating (
    "rating_id"     int                 generated always as identity primary key,
    "submission_id" int                 not null,
    "assessor_id"   int                 not null,
    "is_draft"      boolean             not null,
    "type"          project.rating_type not null,

    constraint rating_submission_fk
        foreign key (submission_id) references project.submission(submission_id),

    constraint rating_assessor_fk
        foreign key (assessor_id) references person.assessor(assessor_id)
);

create index rating_submission_id_idx
    on project.rating(submission_id);

-- ocena_czastkowa_PEM
create table project.partial_rating (
    "partial_rating_id" int generated always as identity primary key,
    "rating_id"         int not null,
    "criterion_id"      int not null,

    "points"            int not null,
    "justification"     text not null,

    "modified"          timestamp not null default current_timestamp,
    "modified_by_id"    int not null,

    constraint rating_partial_rating_fk
        foreign key (rating_id) references project.rating(rating_id),

    constraint pem_cirterion_partial_rating_fk
        foreign key (criterion_id) references edition.pem_criterion(pem_criterion_id)
);

--pytanie_jury
create table project.jury_question (
    "jury_question_id" int     generated always as identity primary key,
    "submission_id"    int     not null,
    "criterion_id"     int     not null,
    "question"         text    not null,
    "is_draft"         boolean not null,

    constraint jury_question_submission_fk
        foreign key (submission_id) references project.submission(submission_id),

    constraint jury_question_criterion_fk
        foreign key (criterion_id) references edition.pem_criterion(pem_criterion_id)
);

create table project.assessors_answer (
    "assessors_answer_id" int generated always as identity primary key,
    "jury_question_id"    int not null,
    "assessor_id"         int not null,
    "answer"              text,
    "files"               text, -- in case of multiple files they are separated by ','

    constraint assessors_answer_jury_question_fk
        foreign key (jury_question_id) references project.jury_question(jury_question_id),

    constraint assessors_answer_not_empty
        check (not (
            (answer = '' is not false ) and (files = '' is not false) -- we dont want both empty
        ))
);

--zgloszenie_projektu_ekspert_IPMA
create table project.ipma_expert_submission (
    "ipma_expert_submission_id" int generated always as identity primary key,
    "ipma_expert_id"            int not null,
    "submission_id"             int not null,

    constraint ipma_expert_submission_ipma_expert_fk
        foreign key (ipma_expert_id) references person.ipma_expert(ipma_expert_id),

    constraint ipma_expert_submission_submission_fk
        foreign key (submission_id) references project.submission(submission_id)
);

create index ipma_expert_submission_ipma_expert_id_idx
    on project.ipma_expert_submission(ipma_expert_id);

create index ipma_expert_submission_submission_id_idx
    on project.ipma_expert_submission(submission_id);
