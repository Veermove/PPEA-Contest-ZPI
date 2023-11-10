import { RatingType, RatingsDTO } from "@/services/clap/model/rating";

// TODO currently it doesn't work due to incorrect data from backend (responses should be null)
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
