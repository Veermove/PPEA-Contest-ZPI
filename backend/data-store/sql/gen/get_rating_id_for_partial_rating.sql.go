// Code generated by sqlc. DO NOT EDIT.
// versions:
//   sqlc v1.24.0
// source: get_rating_id_for_partial_rating.sql

package queries

import (
	"context"
)

const getRatingIdForPartialRating = `-- name: GetRatingIdForPartialRating :one
select
    rating.rating_id   as "rating_id"
from project.rating as rating
inner join project.partial_rating as part_rating on part_rating.rating_id = rating.rating_id
where
    part_rating.partial_rating_id = $1
`

func (q *Queries) GetRatingIdForPartialRating(ctx context.Context, partialRatingID int32) (int32, error) {
	row := q.db.QueryRow(ctx, getRatingIdForPartialRating, partialRatingID)
	var rating_id int32
	err := row.Scan(&rating_id)
	return rating_id, err
}
