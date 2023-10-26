import { PEMCriterion } from "@/services/clap/model/criterion";
import { RatingType } from "@/services/clap/model/rating";
import { AssessorsRatings } from "@/services/clap/model/submission";
import SingleCriterion from "./singleCriterion";

function buildCriteriaTranslationKey(criteria: PEMCriterion) {
  return `criteria.${criteria.area}.${criteria.criteria}${!!criteria.subcriteria ? `.${criteria.subcriteria}` : ''}`
}

function Ratings({ ratings, criteria, type }: { ratings: AssessorsRatings[], criteria: PEMCriterion[], type: RatingType }) {
  return (
    <>
      {criteria.map(criterion => {
        return (
          <SingleCriterion
            assessorsRatings={ratings}
            translatedName={buildCriteriaTranslationKey(criterion)}
            type={type}
            id={criterion.pemCriterionId}
          />
        )
      })}
    </>
  )
}

export default Ratings
