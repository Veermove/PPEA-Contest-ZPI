'use client'

import { useTranslation } from "@/app/i18n/client";
import Error from "@/components/error";
import Ratings from "@/components/ratings/ratings";
import Spinner from "@/components/spinner";
import { useAuthContext } from "@/context/authContext";
import { useClapAPI } from "@/context/clapApiContext";
import { RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { SubmissionDTO } from "@/services/clap/model/submission";
import { redirect, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
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
        setAssessorId((await user.getIdTokenResult()).claims.assessor_id as number);
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

  if (loading) {
    return <Spinner />;
  } else if (!user) {
    return redirect('/login')
  } else if (!submission || !assessorId || error) {
    return <Error text={t('noRatings')} />
  } else if (!ratings?.individualRatings.find(rating => rating.assessorId === assessorId)) {
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
          <Col xs={12}>
            <Button className="btn btn-secondary text-white" onClick={() => router.push(`/assessor/submissions/${submission.submissionId}`)}>
              {t('submissionDetails')}
            </Button>
          </Col>
        </Row>
        <Row>
          <Ratings ratings={ratings} criteria={ratings.criteria} type={RatingType.INDIVIDUAL} assessors={submission.assessors} assessorId={assessorId}/>
        </Row>
      </Container>
    )
  }
}

export default IndividualRatings
