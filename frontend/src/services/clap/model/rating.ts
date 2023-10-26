import { PEMCriterion } from "./criterion";
import { AssessorsRatings } from "./submission";

export enum RatingType {
    INDIVIDUAL = "individual",
    INITIAL = "initial",
    FINAL = "final"
}

export interface Rating {
    ratingId: number;
    isDraft: boolean;
    points: number;
    assessorId: number;
    type: RatingType;
}

export interface PartialRating {
    partialRatingId: number;
    points: number;
    justification: string;
    lastModified: Date;
}

export interface RatingsDTO {
    individualRatings: AssessorsRatings[];
    initialRating?: AssessorsRatings;
    finalRating?: AssessorsRatings;
    criteria: PEMCriterion[];
}
