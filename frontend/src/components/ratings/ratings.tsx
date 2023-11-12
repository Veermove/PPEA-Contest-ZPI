'use client'

import { PEMCriterion } from "@/services/clap/model/criterion";
import { RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { Assessor } from "@/services/clap/model/submission";
import SingleCriterion from "./singleCriterion";

function Ratings({ ratings, criteria, type, assessors, assessorId }: { ratings: RatingsDTO, criteria: PEMCriterion[], type: RatingType, assessors: Assessor[], assessorId: number }) {
  return (
    <>
      {criteria.map(criterion => {
        return (
          <SingleCriterion
            key={criterion.criterionId}
            ratingsDTO={ratings}
            criterionName={criterion.name}
            type={type}
            id={criterion.criterionId}
            currentAssessorId={assessorId}
            assessors={assessors}
          />
        )
      })}
    </>
  )
}

export default Ratings
