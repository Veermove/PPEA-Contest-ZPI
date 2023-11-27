'use client'

import { useTranslation } from "@/app/i18n/client";
import { useAuthContext } from "@/context/authContext";
import { useClapAPI } from "@/context/clapApiContext";
import { PartialRating, RatingType } from "@/services/clap/model/rating";
import { CONFLICT } from "http-status";
import { redirect } from "next/navigation";
import { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import { Button, Col, Container, Row } from "react-bootstrap";
import ErrorComponent from "../error";
import Spinner from "../spinner";
import EditableRating from "./editableRating";

export type SingleRatingForwardData = {
  getIsReadyToSubmit: () => boolean;
}

interface SingleRatingProps {
  firstName: string;
  lastName: string;
  isEditable: boolean;
  criterionId?: number;
  ratingId?: number;
  isEditing: boolean;
  setIsEditing: (isEditing: boolean) => void;
  currentRating?: PartialRating;
  setCurrentRating: (currentRating: PartialRating) => void;
  assessorId: number;
  type: RatingType;
  draft: boolean;
}

const SingleRating = forwardRef<SingleRatingForwardData, SingleRatingProps>(({
  firstName,
  lastName,
  isEditable,
  criterionId,
  ratingId,
  isEditing,
  setIsEditing,
  currentRating,
  setCurrentRating,
  assessorId,
  type,
  draft
}, ref) => {
  const { t } = useTranslation('ratings/singleRating')
  const { user } = useAuthContext();
  const clapAPI = useClapAPI();
  const [error, setError] = useState('');
  const [internalCurrentRating, setInternalCurrentRating] = useState<PartialRating | undefined>(currentRating);

  useEffect(() => {
    if (!user) {
      redirect('/');
    }
  }, [user]);

  useEffect(() => {
    if (currentRating) {
      setInternalCurrentRating(currentRating);
    }
  }, [currentRating])

  function buildErrorMessage(err: any): string {
    console.error(err);

    if (!(err instanceof Error)) {
      return t('error');
    }

    const error = err as Error;

    if (!error.message) {
      return t('error')
    } if (error.message.includes(`${CONFLICT}`)) {
      return t('justificationWasEditedError')
    } if (error.message.includes('length')) {
      return t('justificationLengthError');
    }
    return t('error');
  }

  useEffect(() => {
    if (internalCurrentRating) {
      setCurrentRating(internalCurrentRating);
    }
  }, [internalCurrentRating, setCurrentRating])

  const updateRating = async (justification: string, points: number) => {
    if (!clapAPI || !internalCurrentRating) {
      return;
    }

    try {
      setError('');
      const response = await clapAPI.upsertPartialRating({
        partialRatingId: internalCurrentRating.partialRatingId,
        justification,
        points,
        modified: internalCurrentRating?.modified,
      });
      setInternalCurrentRating(response)
      setIsEditing(false);
    } catch (error) {
      console.error('Unable to update rating:', error);
      setError(buildErrorMessage(error));
    }
  };

  const addRating = async (justification: string, points: number) => {
    setError('');
    if (!clapAPI || !assessorId || !criterionId || !ratingId) {
      return;
    }

    try {
      const response = await clapAPI.upsertPartialRating({
        justification,
        points,
        criterionId,
        ratingId,
      });
      setInternalCurrentRating(response);
      setIsEditing(false);
    } catch (error) {
      console.error('Unable to add rating:', error);
      setError(buildErrorMessage(error));
    }
  }

  const onCancel = () => {
    setIsEditing(false);
    setError('');
  }

  useImperativeHandle(ref, () => ({
    getIsReadyToSubmit: () => {
      return !!internalCurrentRating && !!internalCurrentRating.justification;
    }
  }))

  if (!clapAPI) {
    return <Spinner />
  } else if (!isEditable && (!internalCurrentRating || draft)) { // no rating, current user has no permissions to add
    return <h6 className="text-purple my-4">{t('noRatingsFrom')} {firstName} {lastName}</h6 >
  } else {
    return (
      <Container className="text-left m-0 p-3">
        {error && (
          <Row>
            <Col>
              <ErrorComponent text={error} />
            </Col>
          </Row>
        )}
        <Row>
          <Col xs={6} className="text-purple p-0">
            <h5>{t('assessor')} {firstName} {lastName}</h5>
          </Col>
          <Col xs={6} >
            {t(type)}
          </Col>
        </Row>
        {!internalCurrentRating && !!isEditing && ( // no rating, current user is adding it now
          <Col xs={12} >
            <EditableRating
              initialJustification=""
              initialPoints={0}
              onSubmit={addRating}
              onCancel={onCancel}
            />
          </Col>
        )}{!internalCurrentRating && !isEditing && ( // no rating, current user has permissions to add it
          <Row>
            <Col xs={10} />
            <Col xs={2}>
              <Button className="text-white" onClick={() => setIsEditing(true)}>{t('add')}</Button>
            </Col>
          </Row>
        )}{!!internalCurrentRating && !isEditing && ( // rating exists, in view mode
          <>
            <Row>
              <Col xs={2} className="text-purple font-bold text-wrap">
                {t('points')}: {internalCurrentRating.points} 
              </Col>
              <Col xs={10}>
                {internalCurrentRating.justification}
              </Col>
            </Row>
          </>
        )}{!!internalCurrentRating && !!isEditable && !isEditing && ( // rating exists, in view mode, current user has permissions to edit
          <Row>
            <Col xs={10} />
            <Col xs={2} >
              <Button className="text-white" variant="primary" onClick={() => setIsEditing(true)}>
                {t('edit')}
              </Button>
            </Col>
          </Row>
        )}{!!internalCurrentRating && !!isEditable && !!isEditing && ( // rating exists, in edit mode, current user has permissions to edit
          <Row>
            <Col xs={12} >
              <EditableRating
                initialJustification={internalCurrentRating.justification}
                initialPoints={internalCurrentRating.points}
                onSubmit={updateRating}
                onCancel={onCancel}
              />
            </Col>
          </Row>
        )}
      </Container>
    )
  }
});

SingleRating.displayName = 'SingleRating';

export default SingleRating
