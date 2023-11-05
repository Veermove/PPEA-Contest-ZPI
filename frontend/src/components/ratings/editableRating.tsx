import { useTranslation } from "@/app/i18n/client";
import { Button, Col, Container, Form, Row } from "react-bootstrap";

function EditableRating({ currentDescription, setCurrentDescription, currentRating, setCurrentRating, onSubmit, onCancel }: {
  currentDescription: string,
  setCurrentDescription: (description: string) => void,
  currentRating: number
  setCurrentRating: (rating: number) => void,
  onSubmit: (description: string, rating: number) => void,
  onCancel: () => void
}) {
  const { t } = useTranslation('ratings/editableRating')

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(currentDescription, currentRating);
    onSubmit(currentDescription, currentRating);
  }

  const handleCancel = () => {
    onCancel();
  }

  return (
    <Form onSubmit={handleSubmit} onReset={handleCancel}>
      <Container>
        <Row>
          <Col>
            <Form.Group controlId="ratingDescription">
              <Form.Label>{t('description')}:</Form.Label>
              <Form.Control as="textarea" rows={5} value={currentDescription} onChange={e => setCurrentDescription(e.target.value)} />
            </Form.Group>
          </Col>
          <Col>
            <Form.Group controlId="ratingPoints">
              <Form.Label>{t('rating')}</Form.Label>
              <Form.Control type="number" value={currentRating} step={1} min={0} max={100} onChange={e => setCurrentRating(parseInt(e.target.value))} />
            </Form.Group>
            <Form.Group controlId="ratingButtons">
              <Button className="text-white" variant="primary" type="submit">
                {t('save')}
              </Button>
              <Button className="text-white" type="reset" >
                {t('cancel')}
              </Button>
            </Form.Group>
          </Col>
        </Row>
      </Container>
    </Form>
  )
}

export default EditableRating
