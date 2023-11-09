import { useTranslation } from "@/app/i18n/client";
import { Rating, RatingType } from "@/services/clap/model/rating";
import { Assessor, SubmissionDTO } from "@/services/clap/model/submission";
import { useRouter } from "next/navigation";
import { Accordion, Button, Col, Container, Row } from "react-bootstrap";
import SingleRating from "./singleRating";

function SubmissionOverview({ submission, isActive }: { submission: SubmissionDTO, isActive: boolean }) {
  const { t } = useTranslation('submission/overview');
  const router = useRouter();

  function buildIndividualRatings(ratings: Rating[], assessors: Assessor[]) {
    return (
      <Row className="mt-2 text-black">
        <Row><h6>{t('individual')}:</h6></Row>
        {ratings.map(rating => {
          const assessor = assessors.find(assessor => assessor.assessorId === rating.assessorId)
          return (
            <Row key={rating.ratingId}>
              <Col xs={3} className="font-bold">{assessor?.firstName} {assessor?.lastName}:</Col>
              <Col xs={9}>{rating.isDraft ? t('draftVersion') : t('finalVersion')}</Col>
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
          (!!finalRatings.length ? SingleRating({
            rating: finalRatings[0],
            assessor: submission.assessors.find(assessor => assessor.assessorId === finalRatings[0].assessorId)
          }) :
            !!initialRatings.length ? SingleRating({
              rating: initialRatings[0],
              assessor: submission.assessors.find(assessor => assessor.assessorId === initialRatings[0].assessorId)
            }) :
              !!individualRatings.length && buildIndividualRatings(individualRatings, submission.assessors))
        }
        <Row className="mt-2 text-black">
          <Col xs={4}>{t('assessors')}:</Col>
          <Col xs={8}>
            <ul className="list-disc">
              {submission.assessors.map(assessor => <li key={assessor.assessorId}>{assessor.firstName} {assessor.lastName}</li>)}
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
              <Col xs={9}><h5 className="text-purple">{t('contest')} {submission.year}</h5></Col>
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
