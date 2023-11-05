import { PEMCriterion } from "@/services/clap/model/criterion";
import { RatingType } from "@/services/clap/model/rating";
import { AssessorsRatings } from "@/services/clap/model/submission";
import SingleCriterion from "./singleCriterion";

function Ratings({ ratings, criteria, type }: { ratings: AssessorsRatings[], criteria: PEMCriterion[], type: RatingType }) {
  return (
    <>
      {criteria.map(criterion => {
        return (
          <SingleCriterion
            key={criterion.criterionId}
            assessorsRatings={ratings}
            criterionName={criterion.name}
            type={type}
            id={criterion.criterionId}
          />
        )
      })}
    </>
  )
}

export default Ratings
