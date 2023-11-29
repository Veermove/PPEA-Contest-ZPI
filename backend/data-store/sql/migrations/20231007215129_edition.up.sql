create schema edition;

-- edycja_konkursu
create table edition.contest (
    "contest_id"     int generated always as identity primary key,
    "year"           int not null,
    "master_jury_id" int not null,

    "est_time_individual_assessment"  date,
    "est_time_initial_assessment"     date,
    "est_time_final_assessment"       date,
    "est_time_jury_questions"         date,

    "min_project_duration_days"     int not null,
    "min_participant_team_size"     int not null,
    "min_subcontractors"            int not null,
    "max_project_completion_months" int not null,

    "url_code_of_conduct" varchar(255),
    "url_schedule"        varchar(255),
    "url_flyer"           varchar(255),
    "url_finalists"       varchar(255),
    "url_results"         varchar(255),

    constraint master_jury_fk
        foreign key (master_jury_id) references person.jury_member(jury_member_id)
);

-- czlonek_jury_edycja_konkursu
create table edition.jury_member_contest (
    "jury_member_contest_id" int generated always as identity primary key,
    "jury_member_id"         int not null,
    "contest_id"             int not null,

    constraint jury_member_contest_unique
        unique (jury_member_id, contest_id),

    constraint jury_member_contest_jury_member_fk
        foreign key (jury_member_id) references person.jury_member(jury_member_id),

    constraint jury_member_contest_contest_fk
        foreign key (contest_id) references edition.contest(contest_id)
);

-- przedstawieli_biura_nagrody_edycja_konkursu
create table edition.awards_representative_contest (
    "awards_representative_contest_id" int generated always as identity primary key,
    "awards_representative_id"         int not null,
    "contest_id"                       int not null,

    constraint awards_representative_contest_unique
        unique (awards_representative_id, contest_id),

    constraint awards_representative_contest_awards_representative_fk
        foreign key (awards_representative_id) references person.awards_representative(awards_representative_id),

    constraint awards_representative_contest_contest_fk
        foreign key (contest_id) references edition.contest(contest_id)
);


-- kryterium_PEM
create table edition.pem_criterion (
    "pem_criterion_id" int  generated always as identity primary key,
    "contest_id"       int  not null,
    "name"             text not null,
    "description"      text not null,
    "area"             text not null,
    "criteria"         text not null,
    "subcriteria"      text,

    constraint pem_criterion_contest_fk
        foreign key (contest_id) references edition.contest(contest_id)
);
