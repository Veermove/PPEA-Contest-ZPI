import { useTranslation } from "@/app/i18n/client";
import { PartialRating, RatingType } from "@/services/clap/model/rating";
import { useState } from "react";
import { Button, Col, Container, Row } from "react-bootstrap";
import EditableRating from "./editableRating";

function SingleRating({ partialRating, type, firstName, lastName, isEditable }: { partialRating: PartialRating, type: RatingType, firstName: string, lastName: string, isEditable: boolean }) {
  const { t } = useTranslation('ratings/singleRating')
  const [isEditing, setIsEditing] = useState(false);
  const [currentRating, setCurrentRating] = useState(partialRating)

  function propagateChanges(description: string, rating: number) {
    setCurrentRating({ ...currentRating, justification: description, points: rating })
    setIsEditing(false);
  }

  return (
    <Container className="text-left my-4 mx-0">
      <Row>
        <Col xs={6} className="text-purple p-0"><h5>{t('assessor')} {firstName} {lastName}</h5></Col>
        <Col xs={2} >{t(type)}</Col>
        {!isEditing && (
          <Col Col xs={2} className="text-purple font-bold">{currentRating.points}</Col>
        )}
        {!!isEditable && !isEditing && (
          <Col xs={2} >
            <Button className="text-white" variant="primary" onClick={() => setIsEditing(true)}>
              {t('edit')}
            </Button>
          </Col>)}
      </Row>
      <Row>
        {isEditing ? (
          <EditableRating
            currentDescription={currentRating.justification}
            setCurrentDescription={(description) => setCurrentRating({ ...currentRating, justification: description })}
            currentRating={currentRating.points}
            setCurrentRating={(rating) => setCurrentRating({ ...currentRating, points: rating })}
            onSubmit={propagateChanges}
            onCancel={() => setIsEditing(false)}
          />
        ) : (
          <Col xs={12} className="text-purple">
            {currentRating.justification}
          </Col>
        )}
      </Row>
    </Container>
  )
}

export default SingleRating
