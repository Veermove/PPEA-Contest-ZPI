'use client'

import { useAuthContext } from "@/context/authContext";
import { PEMCriterion } from "@/services/clap/model/criterion";
import { RatingType } from "@/services/clap/model/rating";
import { AssessorsRatings } from "@/services/clap/model/submission";
import { useEffect, useState } from "react";
import SingleCriterion from "./singleCriterion";

function Ratings({ ratings, criteria, type }: { ratings: AssessorsRatings[], criteria: PEMCriterion[], type: RatingType }) {
  const { user } = useAuthContext();
  const [assessorId, setAssessorId] = useState<number>(0);

  useEffect(() => {
    (async () => {
      setAssessorId((await user!.getIdTokenResult()).claims.assessorId as number)
    })()
  }, [user])

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
            currentAssessorId={assessorId}
          />
        )
      })}
    </>
  )
}

export default Ratings
