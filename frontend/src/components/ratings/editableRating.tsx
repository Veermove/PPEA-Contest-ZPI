import { useTranslation } from "@/app/i18n/client";
import { useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import Error from "../error";

function EditableRating({ initialJustification, initialPoints, onSubmit, onCancel }: {
  initialJustification: string,
  initialPoints: number
  onSubmit: (justification: string, rating: number) => void,
  onCancel: () => void
}) {
  const { t } = useTranslation('ratings/editableRating')
  const [error, setError] = useState('');

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget)
    const justification = formData.get('justification')?.toString() || initialJustification
    if (!justification) {
      setError(t('emptyJustification'))
      return;
    }
    const points = parseInt(formData.get('points')?.toString() || '') || initialPoints
    onSubmit(justification, points)
  }

  const handleCancel = () => {
    onCancel();
  }

  return (
    <Form onSubmit={handleSubmit} onReset={handleCancel}>
      <Container>
        {
          !!error && (
            <Row>
              <Error text={error} />
            </Row>
          )
        }
        <Row>
          <Col>
            <Form.Group>
              <Form.Label htmlFor="justification">{t('description')}:</Form.Label>
              <Form.Control name="justification" as="textarea" rows={5} defaultValue={initialJustification} />
            </Form.Group>
          </Col>
          <Col>
            <Form.Group>
              <Form.Label htmlFor="points">{t('rating')}</Form.Label>
              <Form.Control type="number" name="points" defaultValue={initialPoints} step={1} min={0} max={100} />
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
