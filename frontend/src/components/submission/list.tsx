import { useTranslation } from "@/app/i18n/client";
import { SubmissionDTO } from "@/services/clap/model/submission";
import { Container, Row } from "react-bootstrap";
import SubmissionOverview from "./overview";

function splitSubmissionList(submissionList: SubmissionDTO[]): { activeSubmissions: SubmissionDTO[], archiveSubmissions: SubmissionDTO[] } {
  const currentYear = new Date().getFullYear();
  return submissionList.reduce((result: { activeSubmissions: SubmissionDTO[], archiveSubmissions: SubmissionDTO[] }, submission) => {
    result[submission.year === currentYear  ? 'activeSubmissions' : 'archiveSubmissions'].push(submission)
    return result;
  }, { activeSubmissions: [], archiveSubmissions: [] })
}

function SubmissionList({ submissionList }: { submissionList: SubmissionDTO[] }) {

  const { t } = useTranslation('submission/list');

  const { activeSubmissions, archiveSubmissions } = splitSubmissionList(submissionList);
  return (
    <Container className="mt-2">
      <Row>
        {!!activeSubmissions.length && (
          <>
            <h3>{t('activeSubmission')}</h3>
            <SubmissionOverview submission={activeSubmissions[0]} isActive={true} />
          </>
        )}
      </Row>
      <Row className="mt-2">
        {!!archiveSubmissions.length && (
          <>
            <h3>{t('archiveSubmissions')}</h3>
            {archiveSubmissions.map(submission => <SubmissionOverview submission={submission} isActive={false} />)}
          </>
        )}
      </Row>
    </Container>
  )
}

export default SubmissionList;
