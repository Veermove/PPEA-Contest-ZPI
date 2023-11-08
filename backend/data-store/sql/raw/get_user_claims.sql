-- name: GetUserClaims :one
select
    person.person_id,
    person.first_name,
    person.last_name,
    awards_representative.awards_representative_id,
    jury_member.jury_member_id,
    ipma_expert.ipma_expert_id,
    applicant.applicant_id,
    assessor.assessor_id
from person.base as person
left join person.awards_representative as awards_representative
    on person.person_id = awards_representative.person_id
left join person.jury_member as jury_member
    on person.person_id = jury_member.person_id
left join person.ipma_expert as ipma_expert
    on person.person_id = ipma_expert.person_id
left join person.applicant as applicant
    on person.person_id = applicant.person_id
left join person.assessor as assessor
    on ipma_expert.ipma_expert_id = assessor.ipma_expert_id
where person.email = $1;
