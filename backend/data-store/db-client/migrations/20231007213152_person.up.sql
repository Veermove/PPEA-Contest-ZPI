create schema person;

create table person.base (
    "person_id"  int generated always as identity primary key,
    "first_name" text not null,
    "last_name"  text not null,
    "email"      text not null
);

-- przedstawiciel_biura_nagrody
create table person.awards_representative (
    "awards_representative_id" int generated always as identity primary key,
    "person_id"                int unique not null,

    constraint awards_representative_person_fk
        foreign key (person_id) references person.base(person_id)
);

-- czlonek_jury
create table person.jury_member (
    "jury_member_id" int generated always as identity primary key,
    "person_id"      int not null,

    constraint jury_member_person_fk
        foreign key (person_id) references person.base(person_id)
);

-- ekspert_IPMA
create table person.ipma_expert (
    "ipma_expert_id" int generated always as identity primary key,
    "person_id"      int unique not null,

    constraint ipma_expert_person_fk
        foreign key (person_id) references person.base(person_id)
);

-- aplikant
create table person.applicant (
    "applicant_id" int generated always as identity primary key,
    "person_id"    int unique not null,

    constraint applicant_person_fk
        foreign key (person_id) references person.base(person_id)
);

-- asesor
create table person.assessor (
    "assessor_id" int generated always as identity primary key,
    "ipma_expert_id"   int unique not null,

    constraint assessor_person_fk
        foreign key (ipma_expert_id) references person.ipma_expert(ipma_expert_id)
);
