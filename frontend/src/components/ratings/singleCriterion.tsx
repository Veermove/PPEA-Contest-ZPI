import { PartialRating, RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { Assessor, AssessorsRatings } from "@/services/clap/model/submission";
import { forwardRef, useCallback, useEffect, useImperativeHandle, useRef, useState } from "react";
import { Accordion, AccordionHeader, AccordionItem } from "react-bootstrap";
import AccordionBody from "react-bootstrap/esm/AccordionBody";
import SingleRating, { SingleRatingForwardData } from "./singleRating";

export type SingleCriterionForwardData = {
  getIsReadyToSubmit: () => boolean;
  scrollTo: () => void;
}

type SingleCriterionProps = {
  criterionName: string,
  type: RatingType,
  id: number,
  currentAssessorId: number,
  ratingsDTO: RatingsDTO,
  assessors: Assessor[]
}

function calculateIsRated(partialRating?: PartialRating): boolean {
  return !!partialRating && !!partialRating.justification
}

const SingleCriterion = forwardRef<SingleCriterionForwardData, SingleCriterionProps>(({
  criterionName,
  type,
  id,
  currentAssessorId,
  ratingsDTO,
  assessors
}, ref) => {

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
  const [isRated, setIsRated] = useState(false);
  const ratingRef = useRef<SingleRatingForwardData | null>(null);
  const rootRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    let rating: AssessorsRatings | undefined;
    switch (type) {
    case RatingType.FINAL:
      rating = ratingsDTO.finalRating;
      break;
    case RatingType.INITIAL:
      rating = ratingsDTO.initialRating;
      break;
    case RatingType.INDIVIDUAL:
      rating = ratingsDTO.individualRatings.find(assessorRating => assessorRating.assessorId === currentAssessorId)
      break;
    }

    if (!rating) return;
    setEditableRating(toPartialRating(rating));
  }, [type, toPartialRating, ratingsDTO.finalRating, ratingsDTO.initialRating, ratingsDTO.individualRatings, currentAssessorId, ratingRef, id, isEditing])

  useEffect(() => {
    setIsRated(calculateIsRated(editableRating))
  }, [editableRating])

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

  useImperativeHandle(ref, () => ({
    getIsReadyToSubmit: () => {
      const currentRating = ratingRef.current;
      if (!currentRating) return true;
      return currentRating.getIsReadyToSubmit();
    },
    scrollTo: () => {
      if (!rootRef.current) return;
      rootRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }))

  return (
    <div ref={rootRef}>
      <Accordion className="my-2">
        <AccordionItem eventKey={id.toString()}>
          <AccordionHeader>
            <h5 className={isRated ? "text-purple" : "text-gray"}>{criterionName}</h5>
          </AccordionHeader>
          <AccordionBody>
            {type === RatingType.FINAL && !!ratingsDTO.finalRating && (
              <>
                <SingleRating // final rating, editable
                  ref={ratingRef}
                  firstName={ratingsDTO.finalRating.firstName}
                  lastName={ratingsDTO.finalRating.lastName}
                  isEditable={ratingsDTO.finalRating.draft}
                  criterionId={id}
                  ratingId={ratingsDTO.finalRating.ratingId}
                  isEditing={isEditing}
                  setIsEditing={setIsEditing}
                  currentRating={editableRating}
                  setCurrentRating={setEditableRating}
                  assessorId={currentAssessorId}
                  type={RatingType.FINAL}
                  draft={ratingsDTO.finalRating.draft}
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
                    draft={false}
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
                    draft={false}
                  />
                ))}
              </>
            )}
            {type === RatingType.INITIAL && !!ratingsDTO.initialRating && (
              <>
                <SingleRating // initial rating, editable
                  ref={ratingRef}
                  firstName={ratingsDTO.initialRating.firstName}
                  lastName={ratingsDTO.initialRating.lastName}
                  isEditable={ratingsDTO.initialRating.draft}
                  criterionId={id}
                  ratingId={ratingsDTO.initialRating.ratingId}
                  isEditing={isEditing}
                  setIsEditing={setIsEditing}
                  currentRating={editableRating}
                  setCurrentRating={setEditableRating}
                  assessorId={currentAssessorId}
                  type={RatingType.INITIAL}
                  draft={ratingsDTO.initialRating.draft}
                />
                {individualRatings.map(assessorsRating => (
                  <SingleRating // individual rating, not editable
                    ref={ratingRef}
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
                    draft={false}
                  />
                ))}
              </>)}
            {type === RatingType.INDIVIDUAL && (() => {
              const { ownedRating, otherRatings } = splitIndividualRatings(individualRatings);

              return (
                <>
                  <SingleRating // individual rating, editable
                    ref={ratingRef}
                    firstName={ownedRating.firstName}
                    lastName={ownedRating.lastName}
                    isEditable={ownedRating.draft}
                    criterionId={id}
                    ratingId={ownedRating.ratingId}
                    isEditing={isEditing}
                    setIsEditing={setIsEditing}
                    currentRating={editableRating}
                    setCurrentRating={setEditableRating}
                    assessorId={currentAssessorId}
                    type={RatingType.INDIVIDUAL}
                    draft={ownedRating.draft}
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
                      draft={assessorsRating.draft}
                    />
                  ))}
                </>
              );
            })()}
          </AccordionBody>
        </AccordionItem>
      </Accordion>
    </div>
  )
});

SingleCriterion.displayName = 'SingleCriterion';


export default SingleCriterion
