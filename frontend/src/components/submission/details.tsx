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
          <Button className="btn btn-primary m-2 px-2 text-white" onClick={() => router.push(`/ratings/${1}`)}>{t('ratings')}</Button>
        </Col>
      </Row>
      <Row>
        {/* <h1 className="text-center text-purple">{t('submissionHeader')} {submission.name}</h1> */}
        <Row>
          <h1 className="text-left text-purple my-2">{t('submissionHeader')} {submission.name}</h1>
        </Row>
      </Row>
      <Row className="mt-2 justify-content-between divide-x max-w-full">
        <Col xs={6} className="max-w-full">
          <SubmissionDetail value={submissionDetails.durationDays} name="durationDays" valueAlignment="center" />
          <SubmissionDetail value={submissionDetails.teamSize.toString()} name="teamSize" valueAlignment="center" />
          <SubmissionDetail value={submissionDetails.finishDate.toDateString()} name="finishDate" valueAlignment="center" />
          <SubmissionDetail value={submissionDetails.budget} name="budget" valueAlignment="center" />
          {!!submissionDetails.report?.reportSubmissionDate && <SubmissionDetail value={submissionDetails.report!.reportSubmissionDate!.toDateString()} name="reportSubmissionDate" valueAlignment="center" />}
          <SubmissionDescription title="description" description={submissionDetails.description} />
        </Col>
        <Col xs={6} className="border-l-2 border-lightgray mr-0 max-w-full">
          {!!submissionDetails.report?.projectGoals && <SubmissionAttachment url={submissionDetails.report.projectGoals} name={"projectGoals"} />}
          {!!submissionDetails.report?.organisationStructure && <SubmissionAttachment url={submissionDetails.report.organisationStructure} name={"organisationStructure"} />}
          {!!submissionDetails.report?.divisionOfWork && <SubmissionAttachment url={submissionDetails.report?.divisionOfWork!} name="divisionOfWork" />}
          {!!submissionDetails.report?.projectSchedule && <SubmissionAttachment url={submissionDetails.report?.projectSchedule!} name="projectSchedule" />}
          {!!submissionDetails.report?.attachements && <SubmissionAttachment url={submissionDetails.report?.attachements!} name="attachments" />}
        </Col>
      </Row>
    </Container>
  )
}

export default SubmissionDetails
