import { Question } from "@/services/clap/model/studyVisits";
import { Table } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import SingleQuestion from "./singleQuestion";

function SingleVisit({ questions, submissionId }: { questions: Question[], submissionId: number }) {
  const { t } = useTranslation('submission/studyVisits');

  return (
    <Table responsive>
      <thead>
        <tr>
          <th>{t('question')}</th>
          <th>{t('answers')}</th>
          <th>{t('attachments')}</th>
        </tr>
        {questions.map((question, idx) => {
          return (
            <SingleQuestion question={question} submissionId={submissionId} key={`${submissionId}/${idx}`} />
          )
        })}
      </thead>
    </Table>
  )
}

export default SingleVisit;
