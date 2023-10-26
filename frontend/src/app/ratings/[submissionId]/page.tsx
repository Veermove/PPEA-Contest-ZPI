'use client'

import Ratings from "@/components/ratings/ratings";
import { useAuthContext } from "@/context/authContext";
import { ClapApi } from "@/services/clap/api";
import { PEMCriterion } from "@/services/clap/model/criterion";
import { RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { redirect } from "next/navigation";
import { useEffect } from "react";
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

function RatingsForSubmission({ submissionId, submissionName }: { submissionId: number, submissionName: string }) {
  const { user } = useAuthContext()
  const { t } = useTranslation('ratings/ratingsForSubmission')

  let clapApi: ClapApi;

  if (!user) {
    return redirect('/');
  }

  useEffect(() => {
    (async () => {
      clapApi = new ClapApi(await user.getIdToken())
    })
  })

  // const { data: submissionRatings, error } = useSWR<RatingsDTO>(() => clapApi.getSubmissionRatings(submissionId))

  const error = false
  const submissionRatings: RatingsDTO = {
    finalRating: {
      firstName: 'Test',
      lastName: 'Test',
      partialRatings: [
        {
          criterionId: 1,
          justification: 'Test',
          points: 15,
          partialRatingId: 1,
          lastModified: new Date(),
        }, {
          criterionId: 2,
          justification: 'Test',
          points: 55,
          partialRatingId: 2,
          lastModified: new Date(),
        }
      ]
    },
    initialRating: {
      firstName: 'Test',
      lastName: 'Test',
      partialRatings: [
        {
          criterionId: 1,
          justification: `
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget iaculis justo. Praesent facilisis lorem a interdum sagittis. Nullam nunc libero, auctor vel facilisis et, dapibus et nunc. Fusce rhoncus posuere lorem, ut posuere ante vulputate vitae. Nulla non ultrices turpis, id hendrerit nisl. Integer quam sem, tempor blandit dictum auctor, iaculis sit amet justo. Curabitur at volutpat massa, non auctor quam. Curabitur vulputate risus non blandit congue. Vivamus non leo vel arcu condimentum pharetra ac ut magna. Sed egestas id ligula et posuere. Etiam fermentum nibh nisl, quis lobortis mi cursus non. Etiam eu imperdiet libero. Ut vel blandit.
          `,
          points: 35,
          partialRatingId: 3,
          lastModified: new Date(),
        }, {
          criterionId: 2,
          justification: 'Test',
          points: 55,
          partialRatingId: 4,
          lastModified: new Date(),
        }
      ]
    },
    individualRatings: [
      {
        firstName: 'Test',
        lastName: 'Test',
        partialRatings: [
          {
            criterionId: 1,
            justification: 'Test',
            points: 35,
            partialRatingId: 5,
            lastModified: new Date(),
          }, {
            criterionId: 2,
            justification: 'Test',
            points: 55,
            partialRatingId: 6,
            lastModified: new Date(),
          }
        ]
      }, {
        firstName: 'Another',
        lastName: 'Another',
        partialRatings: [
          {
            criterionId: 1,
            justification: `
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget iaculis justo. Praesent facilisis lorem a interdum sagittis. Nullam nunc libero, auctor vel facilisis et, dapibus et nunc. Fusce rhoncus posuere lorem, ut posuere ante vulputate vitae. Nulla non ultrices turpis, id hendrerit nisl. Integer quam sem, tempor blandit dictum auctor, iaculis sit amet justo. Curabitur at volutpat massa, non auctor quam. Curabitur vulputate risus non blandit congue. Vivamus non leo vel arcu condimentum pharetra ac ut magna. Sed egestas id ligula et posuere. Etiam fermentum nibh nisl, quis lobortis mi cursus non. Etiam eu imperdiet libero. Ut vel blandit.
            `,
            points: 35,
            partialRatingId: 7,
            lastModified: new Date(),
          }, {
            criterionId: 2,
            justification: 'Test',
            points: 55,
            partialRatingId: 8,
            lastModified: new Date(),
          }
        ]
      }
    ],
    criteria: [
      {
        area: 'A',
        criteria: '1',
        pemCriterionId: 1,
        name: 'Test',
        description: 'Test',
      }, {
        area: 'B',
        criteria: '1',
        pemCriterionId: 2,
        name: 'Test',
        description: 'Test',
      }
    ]
  }

  if (error
    || !submissionRatings
    || (!submissionRatings.individualRatings.length
      && !submissionRatings.initialRating
      && !submissionRatings.finalRating
    )
  ) {
    // TODO error handling
    console.error(error)
  }

  const { individualRatings, initialRating, finalRating, criteria } = submissionRatings!
  const sortedCriteria = criteria.sort(sortCriteria)

  return (
    <div className="my-2 mx-3">
      <h3 className="my-3 text-purple">{t('submission')}: {submissionName}</h3>
      {!!finalRating && (
        <Accordion className="my-2">
          <AccordionItem eventKey="final">
            <AccordionHeader>
              <h5 className="text-purple">{t('finalRating')}</h5>
            </AccordionHeader>
            <AccordionBody>
              <Ratings
                ratings={[finalRating!]}
                criteria={sortedCriteria}
                type={RatingType.FINAL}
              >
              </Ratings>
            </AccordionBody>
          </AccordionItem>
        </Accordion>
      )}
      {!!initialRating && (
        <Accordion className="my-2">
          <AccordionItem eventKey="initial">
            <AccordionHeader>
              <h5 className="text-purple">{t('initialRating')}</h5>
            </AccordionHeader>
            <AccordionBody>
              <Ratings
                ratings={[initialRating!]}
                criteria={sortedCriteria}
                type={RatingType.INITIAL}
              >
              </Ratings>
            </AccordionBody>
          </AccordionItem>
        </Accordion>
      )}
      {!!individualRatings.length && (
        <Accordion className="my-2">
          <AccordionItem eventKey="individual">
            <AccordionHeader>
              <h5 className="text-purple">{t('individualRatings')}</h5>
            </AccordionHeader>
            <AccordionBody>
              <Ratings
                ratings={individualRatings}
                criteria={sortedCriteria}
                type={RatingType.INDIVIDUAL}
              >
              </Ratings>
            </AccordionBody>
          </AccordionItem>
        </Accordion>
      )}
    </div>
  )
}

export default RatingsForSubmission
