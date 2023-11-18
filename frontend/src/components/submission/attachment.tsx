import { useTranslation } from "@/app/i18n/client";
import { getFileLink } from "@/services/firebase/attachments/getAttachment";
import { useEffect, useState } from "react";
import { Col, Container, Row } from "react-bootstrap";
import { FaPaperclip } from 'react-icons/fa';

function SubmissionAttachment({ fileName, name, submissionId }: { fileName: string, name: string, submissionId: number }) {
  const { t } = useTranslation('submission/attachment');
  const [url, setUrl] = useState('');

  useEffect(() => {
    (async () => {
      try {
        const firebaseUrl = await getFileLink(submissionId, fileName);
        setUrl(firebaseUrl);
      } catch (e) {
        console.error(e);
        setUrl('')
      }
    })();
  }, [fileName, submissionId])


  return (
    <a href={url} className="no-underline visited:no-underline my-2 py-2">
      <Container className="py-3">
        <Row>
          <Col xs={8}>
            <h5 className="text-purple text-left">{t(name)}</h5>
          </Col>
          <Col xs={4} className="text-right">
            <FaPaperclip size={25} className="text-purple ml-auto" />
          </Col>
        </Row>
      </Container>
    </a>
  )
}

export default SubmissionAttachment
