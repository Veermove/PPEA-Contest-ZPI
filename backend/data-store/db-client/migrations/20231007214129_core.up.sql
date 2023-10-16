create schema core

create table core.editable(
    "modified"          timestamp not null,
    "modified_by_id"    int not null,

    constraint person_partial_rating_fk
        foreign key (modified_by_id) references person.base(person_id)
)
