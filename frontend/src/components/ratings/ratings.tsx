import { useTranslation } from "@/app/i18n/client";
import { PEMCriterion } from "@/services/clap/model/criterion";
import { RatingType } from "@/services/clap/model/rating";
import { AssessorsRatings } from "@/services/clap/model/submission";
import SingleCriterion from "./singleCriterion";

function buildCriteriaTranslationKey(criteria: PEMCriterion) {
  return `criteria.${criteria.area}.${criteria.criteria}${!!criteria.subcriteria ? `.${criteria.subcriteria}` : ''}`
}

function Ratings({ ratings, criteria, type }: { ratings: AssessorsRatings[], criteria: PEMCriterion[], type: RatingType }) {
  const { t } = useTranslation('ratings/ratings')
  
  return (
    <>
      {criteria.map(criterion => {
        return (
          <SingleCriterion
            assessorsRatings={ratings}
            translatedName={t(buildCriteriaTranslationKey(criterion))}
            type={type}
            id={criterion.pemCriterionId}
          />
        )
      })}
    </>
  )
}

export default Ratings
