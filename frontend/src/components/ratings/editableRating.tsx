import { useTranslation } from "@/app/i18n/client";
import { useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";

function EditableRating({ currentDescription, currentRating }: { currentDescription: string, currentRating: number }) {
  const { t } = useTranslation('ratings/editableRating')
  const [description, setDescription] = useState(currentDescription);
  const [rating, setRating] = useState(currentRating);

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log(description, rating);
  }
  
  return (
    <Form onSubmit={() => handleSubmit}>
      <Container>
        <Row>
          <Col>
            <Form.Group controlId="ratingDescription">
              <Form.Label>{t('description')}</Form.Label>
              <Form.Control as="textarea" rows={5} value={currentDescription} onChange={e => setDescription(e.target.value)} />
            </Form.Group>
          </Col>
          <Col>
            <Form.Group controlId="ratingPoints">
              <Form.Label>{t('rating')}</Form.Label>
              <Form.Control type="number" value={currentRating} step={5} min={0} max={100} onChange={e => setRating(e.target.value)}/>
            </Form.Group>
            <Form.Group controlId="ratingButtons">
              <Button type="submit" value={t('submit')} />
              <Button type="button" value={t('cancel')} />
            </Form.Group>
          </Col>
        </Row>
      </Container>
    </Form>
  )
}

export default EditableRating
