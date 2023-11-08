import { useTranslation } from "@/app/i18n/client";
import { Button, Col, Container, Form, Row } from "react-bootstrap";

function EditableRating({ initialJustification, initialPoints, onSubmit, onCancel }: {
  initialJustification: string,
  initialPoints: number
  onSubmit: (description: string, rating: number) => void,
  onCancel: () => void
}) {
  const { t } = useTranslation('ratings/editableRating')

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget)
    const points = parseInt(formData.get('points')?.toString() || '') || initialPoints
    const description = formData.get('justification')?.toString() || initialJustification
    onSubmit(description, points)
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
              <Form.Label htmlFor="justification">{t('description')}:</Form.Label>
              <Form.Control name="justification" as="textarea" rows={5} value={initialJustification} />
            </Form.Group>
          </Col>
          <Col>
            <Form.Group controlId="ratingPoints">
              <Form.Label htmlFor="points">{t('rating')}</Form.Label>
              <Form.Control type="number" name="points" value={initialPoints} step={1} min={0} max={100} />
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
