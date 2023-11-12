import { PartialRating, RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { Assessor, AssessorsRatings } from "@/services/clap/model/submission";
import { useCallback, useEffect, useState } from "react";
import { Accordion, AccordionHeader, AccordionItem } from "react-bootstrap";
import AccordionBody from "react-bootstrap/esm/AccordionBody";
import SingleRating from "./singleRating";

function isRated(partialRating?: PartialRating): boolean {
  return !!partialRating && !!partialRating.justification
}

function SingleCriterion({ criterionName, type, id, currentAssessorId, ratingsDTO, assessors }: { criterionName: string, type: RatingType, id: number, currentAssessorId: number, ratingsDTO: RatingsDTO, assessors: Assessor[] }) {

  const toPartialRating = useCallback((assessorsRating?: AssessorsRatings): PartialRating | undefined => {
    if (!assessorsRating?.partialRatings) return undefined;
    return assessorsRating.partialRatings.find(partialRating => partialRating.criterionId === id);
  }, [id]);

  const splitIndividualRatings = useCallback((assessorsRatings: AssessorsRatings[]): {
    ownedRating: AssessorsRatings,
    otherRatings: AssessorsRatings[]
  } => {
    return assessorsRatings.reduce((result: { ownedRating: AssessorsRatings, otherRatings: AssessorsRatings[] }, assessorsRating) => {
      if (assessorsRating.assessorId === currentAssessorId) {
        result.ownedRating = assessorsRating;
      } else {
        result.otherRatings.push(assessorsRating);
      }
      return result;
    }, { ownedRating: {} as AssessorsRatings, otherRatings: [] })
  }, [currentAssessorId]);


  const [editableRating, setEditableRating] = useState<PartialRating | undefined>();
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    switch (type) {
    case RatingType.FINAL:
      setEditableRating(toPartialRating(ratingsDTO.finalRating));
      break;
    case RatingType.INITIAL: 
      setEditableRating(toPartialRating(ratingsDTO.initialRating));
      break;
    case RatingType.INDIVIDUAL:
      const ownedRating = ratingsDTO.individualRatings.find(assessorRating => assessorRating.assessorId === currentAssessorId)
      setEditableRating(toPartialRating(ownedRating));
      break;
    }
  }, [type, toPartialRating, ratingsDTO.finalRating, ratingsDTO.initialRating, ratingsDTO.individualRatings, currentAssessorId])

  const individualRatings: AssessorsRatings[] = assessors.map(assessor => {
    return ratingsDTO.individualRatings.find(assessorRating => assessorRating.assessorId === assessor.assessorId) ||
    {
      assessorId: assessor.assessorId,
      firstName: assessor.firstName,
      lastName: assessor.lastName,
      partialRatings: [],
      draft: true,
      ratingId: 0
    }
  });

  return (
    <Accordion className="my-2">
      <AccordionItem eventKey={id.toString()}>
        <AccordionHeader>
          <h5 className={isRated(editableRating) ? "text-purple" : "text-gray"}>{criterionName}</h5>
        </AccordionHeader>
        <AccordionBody>
          {type === RatingType.FINAL && !!ratingsDTO.finalRating && (
            <>
              <SingleRating // final rating, editable
                firstName={ratingsDTO.finalRating.firstName}
                lastName={ratingsDTO.finalRating.lastName}
                isEditable={true}
                criterionId={id}
                ratingId={ratingsDTO.finalRating.ratingId}
                isEditing={isEditing}
                setIsEditing={setIsEditing}
                currentRating={editableRating}
                setCurrentRating={setEditableRating}
                assessorId={currentAssessorId}
                type={RatingType.FINAL}
              />
              {!!ratingsDTO.initialRating && (
                <SingleRating // initial rating, not editable
                  firstName={ratingsDTO.initialRating.firstName}
                  lastName={ratingsDTO.initialRating.lastName}
                  isEditable={false}
                  criterionId={id}
                  ratingId={ratingsDTO.initialRating.ratingId}
                  isEditing={false}
                  setIsEditing={() => { }}
                  currentRating={toPartialRating(ratingsDTO.initialRating)}
                  setCurrentRating={() => { }}
                  assessorId={ratingsDTO.initialRating.assessorId}
                  type={RatingType.INITIAL}
                />
              )}
              {individualRatings.map(assessorsRating => (
                <SingleRating // individual rating, not editable
                  firstName={assessorsRating.firstName}
                  lastName={assessorsRating.lastName}
                  isEditable={false}
                  criterionId={id}
                  ratingId={assessorsRating.ratingId}
                  isEditing={false}
                  setIsEditing={() => { }}
                  currentRating={assessorsRating.draft ? undefined : toPartialRating(assessorsRating)}
                  setCurrentRating={() => { }}
                  assessorId={assessorsRating.assessorId}
                  type={RatingType.INDIVIDUAL}
                  key={assessorsRating.assessorId}
                />
              ))}
            </>
          )}
          {type === RatingType.INITIAL && !!ratingsDTO.initialRating && (
            <>
              <SingleRating // initial rating, editable
                firstName={ratingsDTO.initialRating.firstName}
                lastName={ratingsDTO.initialRating.lastName}
                isEditable={true}
                criterionId={id}
                ratingId={ratingsDTO.initialRating.ratingId}
                isEditing={isEditing}
                setIsEditing={setIsEditing}
                currentRating={editableRating}
                setCurrentRating={setEditableRating}
                assessorId={currentAssessorId}
                type={RatingType.INITIAL}
              />
              {individualRatings.map(assessorsRating => (
                <SingleRating // individual rating, not editable
                  firstName={assessorsRating.firstName}
                  lastName={assessorsRating.lastName}
                  isEditable={false}
                  criterionId={id}
                  ratingId={assessorsRating.ratingId}
                  isEditing={false}
                  setIsEditing={() => { }}
                  currentRating={assessorsRating.draft ? undefined : toPartialRating(assessorsRating)}
                  setCurrentRating={() => { }}
                  assessorId={assessorsRating.assessorId}
                  type={RatingType.INDIVIDUAL}
                  key={assessorsRating.assessorId}
                />
              ))}
            </>)}
          {type === RatingType.INDIVIDUAL && (() => {
            const { ownedRating, otherRatings } = splitIndividualRatings(individualRatings);

            return (
              <>
                <SingleRating // individual rating, editable
                  firstName={ownedRating.firstName}
                  lastName={ownedRating.lastName}
                  isEditable={true}
                  criterionId={id}
                  ratingId={ownedRating.ratingId}
                  isEditing={isEditing}
                  setIsEditing={setIsEditing}
                  currentRating={editableRating}
                  setCurrentRating={setEditableRating}
                  assessorId={currentAssessorId}
                  type={RatingType.INDIVIDUAL}
                />
                {otherRatings.map(assessorsRating => (
                  <SingleRating // other individual ratings, not editable
                    firstName={assessorsRating.firstName}
                    lastName={assessorsRating.lastName}
                    isEditable={false}
                    criterionId={id}
                    ratingId={assessorsRating.ratingId}
                    isEditing={false}
                    setIsEditing={() => { }}
                    currentRating={toPartialRating(assessorsRating)}
                    setCurrentRating={() => { }}
                    assessorId={assessorsRating.assessorId}
                    type={RatingType.INDIVIDUAL}
                    key={assessorsRating.assessorId}
                  />
                ))}
              </>
            );
          })()}
        </AccordionBody>
      </AccordionItem>
    </Accordion>
  )
}

export default SingleCriterion
