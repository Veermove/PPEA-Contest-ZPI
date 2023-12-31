// Code generated by sqlc. DO NOT EDIT.
// versions:
//   sqlc v1.24.0
// source: validate_partial_rating_update.sql

package queries

import (
	"context"
	"time"
)

const validatePartialRating = `-- name: ValidatePartialRating :one

with get_modified_ts as (
    select
        modified at time zone 'utc' as "modified"
    from project.partial_rating
    where partial_rating_id = $1
),
eq_timestamps as (
    select date_trunc('second', $2::timestamp) = date_trunc('second', (select modified from get_modified_ts)) as "eq_timestamps"
)
select (
    case
        when $1::integer in (
            select partial_rating_id from project.partial_rating
        ) then
            case
                when (select eq_timestamps from eq_timestamps) then ''::text
                else (select to_char(modified, 'YYYY-MM-DD"T"HH24:MI:SS.USZ') from get_modified_ts)::text
            end
        else 'id'::text
    end
)::text
`

type ValidatePartialRatingParams struct {
	PartialRatingID int32
	Modified        time.Time
}

func (q *Queries) ValidatePartialRating(ctx context.Context, arg ValidatePartialRatingParams) (string, error) {
	row := q.db.QueryRow(ctx, validatePartialRating, arg.PartialRatingID, arg.Modified)
	var column_1 string
	err := row.Scan(&column_1)
	return column_1, err
}
