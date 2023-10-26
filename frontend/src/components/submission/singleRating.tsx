import { useTranslation } from "@/app/i18n/client";
import { Rating } from "@/services/clap/model/rating";
import { Col, Row } from "react-bootstrap";

function SingleRating({ rating }: { rating: Rating }) {

  const {t} = useTranslation('submission/overview');

  return (
    <Row className="text-black">
      <Col xs={4}>{t(rating.type)}:</Col>
      <Col xs={8}><p className="font-bold">{rating.points}</p></Col>
    </Row>
  )
}

export default SingleRating;
