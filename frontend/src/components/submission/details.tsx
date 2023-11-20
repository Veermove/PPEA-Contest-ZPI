import { useTranslation } from "@/app/i18n/client";
import { SubmissionDTO, SubmissionDetailsDTO } from "@/services/clap/model/submission";
import { useRouter } from "next/navigation";
import { Button, Col, Container, Row } from "react-bootstrap";
import SubmissionAttachment from "./attachment";
import SubmissionDescription from "./description";
import SubmissionDetail from "./detail";

function SubmissionDetails({ submission, submissionDetails }: { submission: SubmissionDTO, submissionDetails: SubmissionDetailsDTO }) {

  const { t } = useTranslation('submission/details');
  const router = useRouter();

  return (
    <Container className="min-w-fit px-3 py-2">
      <Row>
        <Col>
          <Button className="btn btn-secondary m-2 px-2 text-white" onClick={() => router.push('/assessor/submissions')}>{t('back')}</Button>
        </Col>
        <Col>
          <Button className="btn btn-primary m-2 px-2 text-white" onClick={() => router.push(`/ratings/${submission.submissionId}`)}>{t('ratings')}</Button>
        </Col>
      </Row>
      <Row>
        <Row>
          <h1 className="text-left text-purple my-2">{t('submissionHeader')} {submission.name}</h1>
        </Row>
      </Row>
      <Row className="mt-2 justify-content-between divide-x max-w-full">
        <Col xs={6} className="max-w-full">
          <SubmissionDetail value={Math.round(submissionDetails.points).toString()} name="currentRating" valueAlignment="center" />
          <SubmissionDetail value={submissionDetails.teamSize.toString()} name="teamSize" valueAlignment="center" />
          <SubmissionDetail value={new Date(submissionDetails.finishDate).toDateString()} name="finishDate" valueAlignment="center" />
          <SubmissionDetail value={submissionDetails.budget} name="budget" valueAlignment="center" />
          {!!submissionDetails.appReportDto?.reportSubmissionDate && <SubmissionDetail value={new Date(submissionDetails.appReportDto!.reportSubmissionDate!).toDateString()} name="reportSubmissionDate" valueAlignment="center" />}
          <SubmissionDescription title="description" description={submissionDetails.description} />
        </Col>
        <Col xs={6} className="border-l-2 border-lightgray mr-0 max-w-full">
          {!!submissionDetails.appReportDto?.projectGoals && <SubmissionAttachment
            fileName={submissionDetails.appReportDto.projectGoals}
            name={"projectGoals"}
            submissionId={submission.submissionId} />}
            
          {!!submissionDetails.appReportDto?.organisationStructure && <SubmissionAttachment
            fileName={submissionDetails.appReportDto.organisationStructure}
            name={"organisationStructure"}
            submissionId={submission.submissionId} />}

          {!!submissionDetails.appReportDto?.divisionOfWork && <SubmissionAttachment
            fileName={submissionDetails.appReportDto?.divisionOfWork!}
            name="divisionOfWork"
            submissionId={submission.submissionId} />}

          {!!submissionDetails.appReportDto?.projectSchedule && <SubmissionAttachment
            fileName={submissionDetails.appReportDto?.projectSchedule!}
            name="projectSchedule"
            submissionId={submission.submissionId} />}

          {!!submissionDetails.appReportDto?.attachements && <SubmissionAttachment
            fileName={submissionDetails.appReportDto?.attachements!}
            name="attachments"
            submissionId={submission.submissionId} />}
        </Col>
      </Row>
    </Container>
  )
}

export default SubmissionDetails
