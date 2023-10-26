import { useTranslation } from "@/app/i18n/client";
import { Col, Container, Row } from "react-bootstrap";
import { FaPaperclip } from 'react-icons/fa';

function SubmissionAttachment({ url, name }: { url: string, name: string }) {
  const { t } = useTranslation('submissionAttachment');
  return (
    <a href={url} className="no-underline visited:no-underline my-2">
      <Container className="py-3">
        <Row>
          <Col xs={8}>
            <h5 className="text-purple text-left">{t(name)}</h5>
          </Col>
          <Col xs={4} className="text-right">
            <FaPaperclip size={25} className="text-purple" />
          </Col>
        </Row>
      </Container>
    </a>
  )
}

export default SubmissionAttachment
