'use client'

import { useTranslation } from "@/app/i18n/client";
import Error from "@/components/error";
import Ratings, { RatingsForwardData } from "@/components/ratings/ratings";
import Spinner from "@/components/spinner";
import { useAuthContext } from "@/context/authContext";
import { useClapAPI } from "@/context/clapApiContext";
import { RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { SubmissionDTO } from "@/services/clap/model/submission";
import { redirect, useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import { Button, Col, Container, Row } from "react-bootstrap";

function IndividualRatings() {
  const { t } = useTranslation('ratings/page');
  const clapApi = useClapAPI();
  const router = useRouter();

  const [loading, setLoading] = useState<boolean>(true);
  const [submission, setSubmission] = useState<SubmissionDTO | undefined>(undefined);
  const [ratings, setRatings] = useState<RatingsDTO>();
  const [error, setError] = useState<boolean>(false);
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
        const submissionRatings = await clapApi!.getSubmissionRatings(currentSubmission.submissionId)
        setRatings(submissionRatings)
      } catch {
        setError(true);
      } finally {
        setLoading(false);
      }
    })()
  }, [clapApi, user, assessorId])

  const handleSubmit = async () => {
    const ownedRatingId = ratings?.individualRatings.find(rating => rating.assessorId === assessorId)?.ratingId;
    if (!ratingsRef.current || !ownedRatingId) {
      console.error('no idea what happened')
      // some error
    }
    const isValid = ratingsRef.current!.handleSubmit();
    if (!isValid) {
      return;
    }
    try {
      await clapApi?.submitRatingDraft(ownedRatingId!);
      console.log('all good')
      window.location.reload();
    } catch(e) {
      console.error(e);
      // error handling
    }
  }

  if (loading) {
    return <Spinner />;
  } else if (!user) {
    return redirect('/login')
  } else if (!submission || !assessorId || !ratings || error) {
    return <Error text={t('noRatings')} />
  } else if (!ratings?.individualRatings.some(rating => rating.assessorId === assessorId)) {
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
          <Col xs={6}>
            <Button className="btn btn-secondary text-white" onClick={() => router.push(`/assessor/submissions/${submission.submissionId}`)}>
              {t('submissionDetails')}
            </Button>
          </Col>
          {/* TODO check if the rating status allows submitting the ratings */}
          <Col xs={6} className="text-right">
            <Button className="btn btn-primary text-white" onClick={handleSubmit}>
              {t('submit')}
            </Button>
          </Col>
        </Row>
        <Row>
          <Ratings ref={ratingsRef} ratings={ratings} criteria={ratings.criteria} type={RatingType.INDIVIDUAL} assessors={submission.assessors} assessorId={assessorId}/>
        </Row>
      </Container>
    )
  }
}

export default IndividualRatings
