'use client'

import { PEMCriterion } from "@/services/clap/model/criterion";
import { RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { Assessor } from "@/services/clap/model/submission";
import { forwardRef, useEffect, useImperativeHandle, useRef } from "react";
import SingleCriterion, { SingleCriterionForwardData } from "./singleCriterion";

type RatingsProps = {
  ratings: RatingsDTO,
  criteria: PEMCriterion[],
  type: RatingType,
  assessors: Assessor[],
  assessorId: number
}

export type RatingsForwardData = {
  handleSubmit: () => boolean;
}

const Ratings = forwardRef<RatingsForwardData, RatingsProps>(({
  ratings,
  criteria,
  type,
  assessors,
  assessorId
}, ref) => {

  const criterionRefs = useRef<SingleCriterionForwardData[] | null[]>([]);

  useEffect(() => {
    criterionRefs.current = criterionRefs.current.slice(0, criteria.length);
  }, [criteria]);

  useImperativeHandle(ref, () => ({
    handleSubmit: () => {
      const criteria = criterionRefs.current;
      if (!criteria) {
        return false;
      }
      const notReady = criteria.find(criterion => !criterion?.getIsReadyToSubmit());
      if (!notReady) {
        return true;
      }
      notReady.scrollTo();
      return false;
    }
  }))

  return (
    <>
      {criteria.map((criterion, index) => {
        return (
          <SingleCriterion
            key={criterion.criterionId}
            ref={el => criterionRefs.current[index] = el}
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
)

Ratings.displayName = 'Ratings';


export default Ratings
