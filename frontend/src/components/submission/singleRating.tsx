import { useTranslation } from "@/app/i18n/client";
import { Rating } from "@/services/clap/model/rating";
import { Assessor } from "@/services/clap/model/submission";
import { Col, Row } from "react-bootstrap";

function SingleRating({ rating, assessor }: { rating: Rating, assessor?: Assessor }) {

  const { t } = useTranslation('submission/overview');

  const buildDisplayedText = () => {
    return t(rating.type.toLocaleLowerCase()) + (!!assessor ? ` ${t('givenBy')} ${assessor?.firstName} ${assessor?.lastName} ` : '') + (!!rating.isDraft ? `(${t('draftVersion')})` : `(${t('finalVersion')})`);
  }

  return (
    <Row className="text-black">
      <Col>{buildDisplayedText()} </Col>
    </Row>
  )
}

export default SingleRating;
