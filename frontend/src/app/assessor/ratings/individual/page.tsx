'use client'

import { useTranslation } from "@/app/i18n/client";
import ErrorComponent from "@/components/error";
import Ratings, { RatingsForwardData } from "@/components/ratings/ratings";
import Spinner from "@/components/spinner";
import { useAuthContext } from "@/context/authContext";
import { useClapAPI } from "@/context/clapApiContext";
import { RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { ProjectState, SubmissionDTO, SubmissionDetailsDTO } from "@/services/clap/model/submission";
import { redirect, useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import { Button, Col, Container, Row } from "react-bootstrap";

function IndividualRatings() {
  const { t } = useTranslation('ratings/page');
  const clapApi = useClapAPI();
  const router = useRouter();

  const [loading, setLoading] = useState<boolean>(true);
  const [submission, setSubmission] = useState<SubmissionDTO | undefined>(undefined);
  const [submissionDetails, setSubmissionDetails] = useState<SubmissionDetailsDTO | undefined>(undefined);
  const [ratings, setRatings] = useState<RatingsDTO>();
  const [error, setError] = useState<boolean>(false);
  const [submitError, setSubmitError] = useState<string>('');
  const [assessorId, setAssessorId] = useState(0);

  const { user } = useAuthContext();

  const ratingsRef = useRef<RatingsForwardData | null>(null);

  const addRating = async () => {
    if (!clapApi || !submission) {
      return;
    }
    await clapApi.createRating(submission.submissionId, {
      ratingType: RatingType.INDIVIDUAL,
    });

    window.location.reload();
  }

  useEffect(() => {
    if (!clapApi || !user) {
      return;
    }
    (async () => {
      try {
        setLoading(true);
        const assessor_id = (await user.getIdTokenResult()).claims.assessor_id as number;
        setAssessorId(assessor_id);
        const submissions = await clapApi!.getSubmissions();
        const currentYear = new Date().getFullYear();
        const currentSubmission = submissions.find(s => s.year === currentYear)
        if (!currentSubmission) {
          return;
        }
        setSubmission(currentSubmission);
        const submissionDetails = clapApi!.getSubmissionDetails(currentSubmission.submissionId);
        const submissionRatings = clapApi!.getSubmissionRatings(currentSubmission.submissionId)
        setSubmissionDetails(await submissionDetails);
        setRatings(await submissionRatings)
      } catch {
        setError(true);
      } finally {
        setLoading(false);
      }
    })()
  }, [clapApi, user, assessorId])

  const handleSubmit = async () => {
    setSubmitError('');
    const ownedRatingId = ratings?.individualRatings.find(rating => rating.assessorId === assessorId)?.ratingId;
    if (!ratingsRef.current || !ownedRatingId) {
      setSubmitError(t('submitUnknownError'))
    }
    const isValid = ratingsRef.current!.handleSubmit();
    if (!isValid) {
      setSubmitError(t('notAllRatingsFilled'));
      return;
    }
    try {
      await clapApi?.submitRatingDraft(ownedRatingId!);
      window.location.reload();
    } catch (e) {
      console.error(e);
      setSubmitError(t('submitUnknownError'))
    }
  }

  if (loading) {
    return <Spinner />;
  } else if (!user) {
    return redirect('/')
  } else if (!submission || !assessorId || !ratings || error) {
    return <ErrorComponent text={t('noRatings')} />
  } else if (!ratings?.individualRatings.some(rating => rating.assessorId === assessorId) && submissionDetails?.status === ProjectState.ACCEPTED) {
    return (
      <Container className="m-2">
        <Row>
          <Col xs={2}>
            <Button className="text-white" onClick={addRating}>
              {t('addNewRating')}
            </Button>
          </Col>
        </Row>
      </Container>
    )
  }
  else {
    return (
      <Container className="mx-4 my-2">
        <Row>
          <Col xs={2}>
            <Button className="btn btn-secondary text-white" onClick={() => router.push(`/assessor/submissions/${submission.submissionId}`)}>
              {t('submissionDetails')}
            </Button>
          </Col>
          {submissionDetails && submissionDetails?.status === ProjectState.ACCEPTED &&
            ratings?.individualRatings.find(rating => rating.assessorId === assessorId)?.draft && (
            <Col xs={10}>
              <Row>
                <Col xs={8}>
                  {!!submitError && (
                    <ErrorComponent text={submitError} />
                  )}
                </Col>
                <Col xs={4} className="text-right">
                  <Button className="btn btn-primary text-white" onClick={handleSubmit}>
                    {t('submit')}
                  </Button>
                </Col>
              </Row>
            </Col>
          )}
        </Row>
        <Row>
          <Ratings ref={ratingsRef} ratings={ratings} criteria={ratings.criteria} type={RatingType.INDIVIDUAL} assessors={submission.assessors} assessorId={assessorId} />
        </Row>
      </Container>
    )
  }
}

export default IndividualRatings
