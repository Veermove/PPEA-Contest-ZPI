-- name: GetAnswersForQuestion :many
select
    assessors_answer.answer as "answer",
    assessors_answer.files  as "files"
from project.assessors_answer as assessors_answer
where assessors_answer.jury_question_id = $1;
