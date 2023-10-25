import { useTranslation } from "@/app/i18n/client";
import { Col, Container, Row } from "react-bootstrap";

function SubmissionDetail({ value, name, valueAlignment = "center" }: { value: string, name: string, valueAlignment: string }) {
  const { t } = useTranslation('submissionDetail');
  return (
      <Container className="py-2">
        <Row className="justify-content-between align-items-center">
          <Col xs={6}>
            <h5 className="text-purple text-left">{t(name)}:</h5>
          </Col>
          <Col xs={6} className="justify-content-end">
            <div className="bg-lightgray rounded-md py-1 px-2">
                <h6 className={`text-${valueAlignment} text-purple`}>value</h6>
            </div>
          </Col>
        </Row>
      </Container>
  )
}

export default SubmissionDetail
