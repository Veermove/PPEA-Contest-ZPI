// Code generated by sqlc. DO NOT EDIT.
// versions:
//   sqlc v1.24.0
// source: get_partial_ratings_for_assessor_rating.sql

package queries

import (
	"context"
	"time"
)

const getPRatingsForAssessorAndRatingID = `-- name: GetPRatingsForAssessorAndRatingID :many
select
    rating.partial_rating_id,
    rating.criterion_id,
    rating.points,
    rating.justification,
    rating.modified,
    rating.modified_by_id
from project.rating as full_rating
inner join project.partial_rating as rating on full_rating.rating_id = rating.rating_id
where full_rating.rating_id = $1
    and full_rating.assessor_id = $2
`

type GetPRatingsForAssessorAndRatingIDParams struct {
	RatingID   int32
	AssessorID int32
}

type GetPRatingsForAssessorAndRatingIDRow struct {
	PartialRatingID int32
	CriterionID     int32
	Points          int32
	Justification   string
	Modified        time.Time
	ModifiedByID    int32
}

func (q *Queries) GetPRatingsForAssessorAndRatingID(ctx context.Context, arg GetPRatingsForAssessorAndRatingIDParams) ([]GetPRatingsForAssessorAndRatingIDRow, error) {
	rows, err := q.db.Query(ctx, getPRatingsForAssessorAndRatingID, arg.RatingID, arg.AssessorID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	var items []GetPRatingsForAssessorAndRatingIDRow
	for rows.Next() {
		var i GetPRatingsForAssessorAndRatingIDRow
		if err := rows.Scan(
			&i.PartialRatingID,
			&i.CriterionID,
			&i.Points,
			&i.Justification,
			&i.Modified,
			&i.ModifiedByID,
		); err != nil {
			return nil, err
		}
		items = append(items, i)
	}
	if err := rows.Err(); err != nil {
		return nil, err
	}
	return items, nil
}
