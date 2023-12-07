drop schema if exists validation_checks cascade;

alter table if exists project.partial_rating
drop constraint if exists partial_rating_correct_status;
