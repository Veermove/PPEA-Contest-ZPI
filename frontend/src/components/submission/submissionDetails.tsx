import { useTranslation } from "@/app/i18n/client";
import { SubmissionDTO, SubmissionDetailsDTO } from "@/services/clap/model/submission";
import { useRouter } from "next/navigation";
import { Button, Col, Container, Row } from "react-bootstrap";
import SubmissionAttachment from "./submissionAttachment";
import SubmissionDescription from "./submissionDescription";
import SubmissionDetail from "./submissionDetail";

function SubmissionDetails({ submission, submissionDetails }: { submission: SubmissionDTO, submissionDetails: SubmissionDetailsDTO }) {

  const { t } = useTranslation('submissionDetails');
  const router = useRouter();

  const testText =
    `
  Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum non ex ut neque egestas sodales. Aliquam interdum libero et dui accumsan molestie. Integer tempor est quis hendrerit pellentesque. Fusce eget mattis diam. Duis molestie eros libero, eu pretium diam varius in. Aliquam dapibus arcu nisl. Phasellus dignissim tortor eu leo tristique hendrerit. Fusce at tincidunt dui. Nullam condimentum libero vel pellentesque maximus. Suspendisse sodales non orci vel cursus. Curabitur varius convallis sem, sed finibus purus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec nisl lectus, aliquet quis lorem non, pulvinar congue libero. Morbi faucibus, elit ac lacinia posuere, leo velit maximus ante, vel porttitor nibh lacus eget metus. Vestibulum suscipit, eros eget venenatis laoreet, arcu odio mattis quam, eget sagittis risus nisi quis metus. Vestibulum egestas pretium elit ac dignissim.
  
  Pellentesque pretium lorem elit, in tempor nulla vestibulum eget. Nullam auctor quis ex nec scelerisque. Nam lorem dui, imperdiet eget leo sed, condimentum fermentum mi. Cras scelerisque ac est id feugiat. Pellentesque et risus nulla. Maecenas varius sapien vel leo lobortis, et iaculis ex pharetra. Ut massa nisi, eleifend id ipsum in, laoreet efficitur augue. Nulla facilisi. Cras dignissim quam sed diam pellentesque venenatis. Vestibulum. 
  `

  return (
    <Container className="p-1 min-w-full">
          <Button className="btn btn-secondary m-2 px-2 text-white" onClick={() => router.push('/assessor/submissions')}>{t('back')}</Button>
      <Row>
        {/* <h1 className="text-center text-purple">{t('submissionHeader')} {submission.name}</h1> */}
        <Row>
          <h1 className="text-left text-purple my-2">{t('submissionHeader')}: name</h1>
        </Row>
      </Row>
      <Row className="mt-2 justify-content-between divide-x max-w-full">
        <Col xs={6} className="max-w-full">
          <SubmissionDetail value="test" name="name" valueAlignment="center" />
          <SubmissionDetail value="test" name="name" valueAlignment="left" />
          <SubmissionDetail value="test" name="name" valueAlignment="right" />
          <SubmissionDescription title="test" description={testText} />
        </Col>
        <Col xs={6} className="border-l-2 border-lightgray mr-0 max-w-full">
          <SubmissionAttachment url={"/test"} name={"projectGoals"} />
          <SubmissionAttachment url={"/test"} name={"projectGoals"} />
          <SubmissionAttachment url={"/test"} name={"projectGoals"} />
          <SubmissionAttachment url={"/test"} name={"projectGoals"} />
          <SubmissionAttachment url={"/test"} name={"projectGoals"} />
          <SubmissionAttachment url={"/test"} name={"projectGoals"} />
        </Col>
      </Row>
      {/* TODO it's scrolling horizontally */}
      <Row>
        <Col xs={8}><></></Col>
        <Col xs={4}>
          <Button className="btn btn-primary m-2 px-2 text-white" onClick={() => router.push(`/ratings/${submission.submissionId}`)}>{t('ratings')}</Button>
        </Col>
      </Row>
    </Container>
  )
}

export default SubmissionDetails
