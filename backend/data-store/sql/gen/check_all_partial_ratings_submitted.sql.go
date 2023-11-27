// Code generated by sqlc. DO NOT EDIT.
// versions:
//   sqlc v1.24.0
// source: check_all_partial_ratings_submitted.sql

package queries

import (
	"context"
)

const checkAllPartialRatingsSubmitted = `-- name: CheckAllPartialRatingsSubmitted :one
with getRatingCirtsId as (
    select
        part_rating.criterion_id as "criterion_id"
    from
        project.partial_rating as part_rating
        inner join project.rating as rating on rating.rating_id = part_rating.rating_id
        where rating.rating_id = $1
),
getContestCritsId as (
    select
        criterion.pem_criterion_id as "criterion_id"
    from project.rating as rating
        inner join project.submission    as submission on submission.submission_id = rating.submission_id
        inner join edition.pem_criterion as criterion  on criterion.contest_id     = submission.contest_id
    where rating.rating_id = $1
),
diffCrits as (
    select
        contestCrits.criterion_id
    from getContestCritsId as contestCrits
    left join getRatingCirtsId as ratingCrits on ratingCrits.criterion_id = contestCrits.criterion_id
    where ratingCrits.criterion_id is null
)
select (
    (count(*) = 0)
) from diffCrits
`

func (q *Queries) CheckAllPartialRatingsSubmitted(ctx context.Context, ratingID int32) (bool, error) {
	row := q.db.QueryRow(ctx, checkAllPartialRatingsSubmitted, ratingID)
	var column_1 bool
	err := row.Scan(&column_1)
	return column_1, err
}
