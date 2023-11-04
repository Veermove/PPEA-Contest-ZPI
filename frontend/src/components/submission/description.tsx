import { useTranslation } from "@/app/i18n/client";
import { Container } from "react-bootstrap";

function SubmissionDescription({ title, description }: { title: string, description: string }) {
  const { t } = useTranslation('submission/description');
  return (
    <Container className="py-2">
      <h5 className="text-purple text-left">{t(title)}:</h5>
      <div className="bg-lightgray rounded-md p-2">
        <p className="text-purple">{description}</p>
      </div>
    </Container>
  )
}

export default SubmissionDescription
