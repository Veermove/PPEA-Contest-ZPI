import { useTranslation } from "@/app/i18n/client";
import { useState } from "react";
import { Button } from "react-bootstrap";
import EditableRating from "./editableRating";

function NewPartialRating({onSubmit, onCancel}: {onSubmit: (description: string, points: number) => void, onCancel: () => void}) {
  const { t } = useTranslation('ratings/newPartial');
  const [isExpanded, setIsExpanded] = useState(false);
  
  const handleCancel = () => {
    setIsExpanded(false);
    onCancel();
  }

  const handleSubmit = (description: string, points: number) => {
    setIsExpanded(false)
    onSubmit(description, points)
  }

  return isExpanded ? (
    <EditableRating initialJustification="" initialPoints={0} onSubmit={handleSubmit} onCancel={handleCancel}/>
  ) : (
    <Button className="btn-primary" onClick={() => setIsExpanded(true)}>{t('add')}</Button>
  )
}

export default NewPartialRating
