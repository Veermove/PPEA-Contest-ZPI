import { useTranslation } from "@/app/i18n/client";
import { Rating, RatingType } from "@/services/clap/model/rating";
import { Assesor, SubmissionDTO } from "@/services/clap/model/submission";
import { useRouter } from "next/navigation";
import { Accordion, Button, Col, Container, Row } from "react-bootstrap";

function SubmissionOverview({ submission, isActive }: { submission: SubmissionDTO, isActive: boolean }) {
    const { t } = useTranslation('submissionOverview');
    const router = useRouter(); 
    
    function buildSingleRating(rating: Rating) {
        return (
            <Row>
                <Col xs={4}>{t(rating.type.toLocaleLowerCase())}:</Col>
                <Col xs={8}>{rating.points}</Col>
            </Row>
        )
    }

    function buildIndividualRatings(ratings: Rating[], assessors: Assesor[]) {
        return (
            <Row>
                <Row><h6>{t('individual')}</h6></Row>
                {ratings.map(rating => {
                    const assessor = assessors.find(assessor => assessor.assesorId === rating.assessorId)
                    return (
                        <Row>
                            <Col xs={6}>{assessor?.firstName} {assessor?.lastName}</Col>
                            <Col xs={6}>{rating.points}</Col>
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
                    (finalRatings.length ? buildSingleRating(finalRatings[0]) :
                        initialRatings.length ? buildSingleRating(initialRatings[0]) :
                            individualRatings.length && buildIndividualRatings(individualRatings, submission.assesors))
                }
                <Row>
                    <Col xs={4}>{t('assessors')}:</Col>
                    <Col xs={8}>
                        <ul>
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
                            <Col xs={6}>{t('contest')} {submission.contest.year}</Col>
                            <Col xs={6}>{submission.name.substring(0, 20)}{submission.name.length > 20 && '...'}</Col>
                        </Row>
                    </Container>
                </Accordion.Header>
                <Accordion.Body>
                    <Container>
                        <Row>
                            <Col xs={4}>
                                <Row>
                                    {submission.name}
                                </Row>
                                <Row>
                                    {isActive && t('ratingMightChange')}
                                </Row>
                                <Row>
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
