import { RatingType, RatingsDTO } from "@/services/clap/model/rating";

export function isSubmissionRatingActive(ratings: RatingsDTO, submissionRatingType: RatingType) {
  switch (submissionRatingType) {
  case RatingType.INDIVIDUAL:
    return !ratings.finalRating && !ratings.initialRating;
  case RatingType.INITIAL:
    return !ratings.finalRating;
  case RatingType.FINAL:
    return true;
  }
}
