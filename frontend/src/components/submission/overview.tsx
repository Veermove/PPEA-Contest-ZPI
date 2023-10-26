import { useTranslation } from "@/app/i18n/client";
import { Rating, RatingType } from "@/services/clap/model/rating";
import { Assesor, SubmissionDTO } from "@/services/clap/model/submission";
import { useRouter } from "next/navigation";
import { Accordion, Button, Col, Container, Row } from "react-bootstrap";
import SingleRating from "./singleRating";

function SubmissionOverview({ submission, isActive }: { submission: SubmissionDTO, isActive: boolean }) {
  const { t } = useTranslation('submission/overview');
  const router = useRouter();

  function buildIndividualRatings(ratings: Rating[], assessors: Assesor[]) {
    return (
      <Row className="mt-2 text-black">
        <Row><h6>{t('individual')}:</h6></Row>
        {ratings.map(rating => {
          const assessor = assessors.find(assessor => assessor.assesorId === rating.assessorId)
          return (
            <Row>
              <Col xs={4}>{assessor?.firstName} {assessor?.lastName}:</Col>
              <Col xs={8} className="font-bold">{rating.points}</Col>
            </Row>
          )
        })}
      </Row>
    )
  }

  function buildRatingDisplay() {
    const finalRatings = submission.ratings.filter(rating => rating.type === RatingType.FINAL)
    const initialRatings = submission.ratings.filter(rating => rating.type === RatingType.INITIAL)
    const individualRatings = submission.ratings.filter(rating => rating.type === RatingType.INDIVIDUAL)
    return (
      <Container>
        {
          (finalRatings.length ? SingleRating({ rating: finalRatings[0] }) :
            initialRatings.length ? SingleRating({ rating: initialRatings[0] }) :
              individualRatings.length && buildIndividualRatings(individualRatings, submission.assesors))
        }
        <Row className="mt-2 text-black">
          <Col xs={4}>{t('assessors')}:</Col>
          <Col xs={8}>
            <ul className="list-disc">
              {submission.assesors.map(assessor => <li key={assessor.assesorId}>{assessor.firstName} {assessor.lastName}</li>)}
            </ul>
          </Col>
        </Row>
      </Container>
    )
  }

  return (
    <Accordion>
      <Accordion.Item eventKey="0">
        <Accordion.Header>
          <Container>
            <Row>
              <Col xs={9}><h5 className="text-purple">{t('contest')} {submission.contest.year}</h5></Col>
              <Col xs={3}><h5 className="text-purple">{submission.name.substring(0, 20)}{submission.name.length > 20 && '...'}</h5></Col>
            </Row>
          </Container>
        </Accordion.Header>
        <Accordion.Body>
          <Container>
            <Row className="divide-x text-gray">
              <Col xs={4}>
                <Row>
                  <h6 className="text-purple">{submission.name}</h6>
                </Row>
                <Row>
                  <p className="text-gray">{isActive && t('ratingMightChange')}</p>
                </Row>
                <Row className="my-2">
                  <Col>
                    <Button className="btn btn-primary text-white" onClick={() => router.push(`/assessor/submissions/${submission.submissionId}`)}>
                      {t('submissionDetails')}
                    </Button>
                  </Col>
                </Row>
              </Col>
              <Col>{buildRatingDisplay()}</Col>
            </Row>
          </Container>
        </Accordion.Body>
      </Accordion.Item>
    </Accordion>
  )
}

export default SubmissionOverview
