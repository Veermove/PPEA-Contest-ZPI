import { useTranslation } from "@/app/i18n/client";
import { PartialRating, RatingType } from "@/services/clap/model/rating";
import { Col, Container, Row } from "react-bootstrap";

function SingleRating({ partialRating, type, firstName, lastName}: {partialRating: PartialRating, type: RatingType, firstName: string, lastName: string}) {
  const {t} = useTranslation('ratings/singleRating')
  
  return (
    <Container>
      <Row>
        <Col xs={6} className="text-purple"><h5>{t('assessor')} {firstName} {lastName}</h5></Col>
        <Col xs={3}>{t(type)}</Col>
        <Col xs={3} className="text-purple font-bold">{partialRating.points}</Col>
      </Row>
      <Row>
        <p>{partialRating.justification}</p>
      </Row>
    </Container>
  )
}

export default SingleRating
