import { useTranslation } from "@/app/i18n/client";
import { RatingType } from "@/services/clap/model/rating";
import { AssessorsRatings } from "@/services/clap/model/submission";
import { Accordion, AccordionHeader, AccordionItem } from "react-bootstrap";
import AccordionBody from "react-bootstrap/esm/AccordionBody";
import SingleRating from "./singleRating";

function SingleCriterion({ assessorsRatings, criterionName, type, id }: { assessorsRatings: AssessorsRatings[], criterionName: string, type: RatingType, id: number }) {
  const { t } = useTranslation('ratings/singleCriterion')

  let isRated = false;
  const ratings = assessorsRatings.map((assessorRating) => {
    const partialRating = assessorRating.partialRatings.find((partialRating) => partialRating.criterionId === id)
    if (partialRating) {
      isRated = true;
      return <SingleRating
        key={partialRating.partialRatingId}
        partialRating={partialRating}
        type={type}
        firstName={assessorRating.firstName}
        lastName={assessorRating.lastName}
        // TODO isEditable condition
        isEditable={true}
      />
    }
    return <h6 className="text-purple my-4" key={`criterion-${id}-${type}`}>{t('noRatingsFrom')} {assessorRating.firstName} {assessorRating.lastName}</h6 >
  });


  return (
    <Accordion className="my-2">
      <AccordionItem eventKey={id.toString()}>
        <AccordionHeader>
          <h5 className={isRated ? "text-purple" : "text-gray"}>{criterionName}</h5>
        </AccordionHeader>
        <AccordionBody>
          {ratings}
        </AccordionBody>
      </AccordionItem>
    </Accordion>
  )
}

export default SingleCriterion
