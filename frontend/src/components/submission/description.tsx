import { useTranslation } from "@/app/i18n/client";

function SubmissionDescription({ title, description }: { title: string, description: string }) {
  const { t } = useTranslation('submission/description');
  return (
    <div>
      <h5 className="text-purple text-left">{t(title)}:</h5>
      <div className="bg-lightgray rounded-md p-2">
        <p className="text-purple">{description}</p>
      </div>
    </div>
  )
}

export default SubmissionDescription
