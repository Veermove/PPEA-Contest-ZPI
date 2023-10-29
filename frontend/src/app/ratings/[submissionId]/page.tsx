'use client'

import Error from "@/components/error";
import Ratings from "@/components/ratings/ratings";
import Spinner from "@/components/spinner";
import { useAuthContext } from "@/context/authContext";
import { ClapApi } from "@/services/clap/api";
import { PEMCriterion } from "@/services/clap/model/criterion";
import { RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";
import { Accordion, AccordionHeader, AccordionItem } from "react-bootstrap";
import AccordionBody from "react-bootstrap/esm/AccordionBody";
import { useTranslation } from "react-i18next";

function sortCriteria(a: PEMCriterion, b: PEMCriterion) {
  if (a.area !== b.area) return a.area.localeCompare(b.area)
  if (a.criteria !== b.criteria) return a.criteria.localeCompare(b.criteria)
  if (!a.subcriteria && !b.subcriteria) return 0
  if (!a.subcriteria) return -1
  if (!b.subcriteria) return 1
  return a.subcriteria.localeCompare(b.subcriteria)
}

function RatingsForSubmission({ params }: { params: {submissionId: string} }) {
  const { user } = useAuthContext()
  const { t } = useTranslation('ratings/ratingsForSubmission')

  let clapApi: ClapApi;

  if (!user) {
    return redirect('/');
  }

  const id = parseInt(params.submissionId)
  if (Number.isNaN(id)) {
    return Error({ text: t('invalidSubmissionId') })
  }

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [ratings, setRatings] = useState<RatingsDTO>();
  const [sortedCriteria, setSortedCriteria] = useState<PEMCriterion[]>([]);

  useEffect(() => {
    (async () => {
      setLoading(true)
      if (!clapApi) {
        clapApi = new ClapApi(await user.getIdToken());
      }
      clapApi.getSubmissionRatings(id).then((ratings) => {
        setRatings(ratings);
        setSortedCriteria(ratings.criteria.sort(sortCriteria))
      })
        .catch((error) => {
          console.error(error);
          setError(true);
        })
        .finally(() => {
          setLoading(false);
        })
    })()
  }, [])

  if (loading) {
    return <Spinner />
  } else if (error || !ratings) {
    return Error({ text: t('error') })
  }
  return (
    <div className="my-2 mx-3">
      {!!ratings.finalRating && (
        <Accordion className="my-2">
          <AccordionItem eventKey="final">
            <AccordionHeader>
              <h5 className="text-purple">{t('finalRating')}</h5>
            </AccordionHeader>
            <AccordionBody>
              <Ratings
                ratings={[ratings.finalRating!]}
                criteria={sortedCriteria}
                type={RatingType.FINAL}
              />
            </AccordionBody>
          </AccordionItem>
        </Accordion>
      )}
      {!!ratings.initialRating && (
        <Accordion className="my-2">
          <AccordionItem eventKey="initial">
            <AccordionHeader>
              <h5 className="text-purple">{t('initialRating')}</h5>
            </AccordionHeader>
            <AccordionBody>
              <Ratings
                ratings={[ratings.initialRating!]}
                criteria={sortedCriteria}
                type={RatingType.INITIAL}
              />
            </AccordionBody>
          </AccordionItem>
        </Accordion>
      )}
      {!!ratings.individualRatings.length && (
        <Accordion className="my-2">
          <AccordionItem eventKey="individual">
            <AccordionHeader>
              <h5 className="text-purple">{t('individualRatings')}</h5>
            </AccordionHeader>
            <AccordionBody>
              <Ratings
                ratings={ratings.individualRatings}
                criteria={sortedCriteria}
                type={RatingType.INDIVIDUAL}
              />
            </AccordionBody>
          </AccordionItem>
        </Accordion>
      )}
    </div>
  )
}

export default RatingsForSubmission
