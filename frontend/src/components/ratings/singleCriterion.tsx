import { useTranslation } from "@/app/i18n/client";
import { RatingType } from "@/services/clap/model/rating";
import { AssessorsRatings } from "@/services/clap/model/submission";
import { Accordion, AccordionHeader, AccordionItem } from "react-bootstrap";
import AccordionBody from "react-bootstrap/esm/AccordionBody";
import SingleRating from "./singleRating";

function SingleCriterion({ assessorsRatings, criterionName, type, id }: { assessorsRatings: AssessorsRatings[], criterionName: string, type: RatingType, id: number }) {
  const { t } = useTranslation('ratings/singleCriterion')

  return (
    <Accordion className="my-2">
      <AccordionItem eventKey={id.toString()}>
        <AccordionHeader>
          <h5 className="text-purple">{criterionName}</h5>
        </AccordionHeader>
        <AccordionBody>
          {assessorsRatings.map((assessorRating) => {
            const partialRating = assessorRating.partialRatings.find((partialRating) => partialRating.criterionId === id)
            return !!partialRating ? (
              <SingleRating
                partialRating={partialRating}
                type={type}
                firstName={assessorRating.firstName}
                lastName={assessorRating.lastName}
              />
            ) : <h6 className="text-purple">{t('noRatingsFrom')} {assessorRating.firstName} {assessorRating.lastName}</h6>
          })
          }
        </AccordionBody>
      </AccordionItem>
    </Accordion>
  )
}

export default SingleCriterion
