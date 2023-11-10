import { useTranslation } from "@/app/i18n/client";
import { useClapAPI } from "@/context/clapApiContext";
import { RatingType } from "@/services/clap/model/rating";
import { AssessorsRatings } from "@/services/clap/model/submission";
import { useState } from "react";
import { Accordion, AccordionHeader, AccordionItem } from "react-bootstrap";
import AccordionBody from "react-bootstrap/esm/AccordionBody";
import NewPartialRating from "./newPartialRating";
import SingleRating from "./singleRating";

function SingleCriterion({ assessorsRatings, criterionName, type, id, currentAssessorId }: { assessorsRatings: AssessorsRatings[], criterionName: string, type: RatingType, id: number, currentAssessorId: number }) {

  function isEditable(assessorsRating: AssessorsRatings) {
    return !assessorsRating.draft && (type === RatingType.INDIVIDUAL ? assessorsRating.assessorId === currentAssessorId : true)
  }

  const [forceUpdate, setForceUpdate] = useState(0);

  const { t } = useTranslation('ratings/singleCriterion')
  const clapApi = useClapAPI();

  let isRated = false;
  const ratings = assessorsRatings.map((assessorRating) => {
    const partialRating = assessorRating.partialRatings.find((partialRating) => partialRating.criterionId === id)
    if (partialRating) {
      const isEditableRating = isEditable(assessorRating)
      isRated = true;
      return <SingleRating
        key={partialRating.partialRatingId}
        partialRating={partialRating}
        type={type}
        firstName={assessorRating.firstName}
        lastName={assessorRating.lastName}
        isEditable={isEditableRating}
      />
    }
    else if (type !== RatingType.INDIVIDUAL || assessorRating.assessorId === currentAssessorId) {
      const onSubmit = async (justification: string, points: number) => {
        try {
          const partialRating = await clapApi!.upsertPartialRating({
            justification,
            points,
            criterionId: id,
            ratingId: assessorRating.ratingId
          });
          assessorRating.partialRatings.push(partialRating);
          setForceUpdate(forceUpdate + 1)
        } catch (error) {
          console.error(error)
        }
      }
      return <NewPartialRating key={`newRating-${id}-${type}-${assessorRating.assessorId}`} onCancel={() => { }} onSubmit={onSubmit} /> 
    }
    return <h6 className="text-purple my-4" key={`criterion-${id}-${type}-${assessorRating.assessorId}`}>{t('noRatingsFrom')} {assessorRating.firstName} {assessorRating.lastName}</h6 >
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
