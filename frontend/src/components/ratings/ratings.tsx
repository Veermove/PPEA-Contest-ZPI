'use client'

import { useAuthContext } from "@/context/authContext";
import { PEMCriterion } from "@/services/clap/model/criterion";
import { RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { Assessor } from "@/services/clap/model/submission";
import { useEffect, useState } from "react";
import SingleCriterion from "./singleCriterion";

function Ratings({ ratings, criteria, type, assessors }: { ratings: RatingsDTO, criteria: PEMCriterion[], type: RatingType, assessors: Assessor[] }) {
  const { user } = useAuthContext();
  const [assessorId, setAssessorId] = useState(0);

  useEffect(() => {
    if (!user) {
      return;
    }
    (async () => {
      setAssessorId((await user!.getIdTokenResult()).claims.assessor_id as number)
    })()
  }, [user])

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
