import Spinner from "@/components/spinner";
import { Question } from "@/services/clap/model/studyVisits";
import { getFileLink } from "@/services/firebase/attachments/getAttachment";
import { useEffect, useState } from "react";
import { Col, OverlayTrigger, Row, Tooltip } from "react-bootstrap";
import { FaPaperclip } from "react-icons/fa";

function SingleQuestion({ question, submissionId, className }: { question: Question, submissionId: number, className: string }) {

  const [urls, setUrls] = useState<string[][] | undefined>(undefined)

  useEffect(() => {
    (async () => {
      const urls = await Promise.all(question.answers.map(async answer => {
        return await Promise.all(answer.files.map(async file => {
          return await getFileLink(submissionId, file)
        }))
      }))
      setUrls(urls)
    })()
  });

  const { answers, content } = question;
  if (!urls) {
    return <Spinner />
  }
  return (
    <Row className={className}>
      <Col xs={4} className="px-2">
        {content}
      </Col>
      <Col xs={6}>
        {answers.map((answer, idx) => {
          return (<Row key={`answer-${idx}`} className="mt-2 mx-1">{answer.answerText}
          </Row>)
        })}
      </Col>
      <Col xs={2}>
        {answers.map((answer, answerIndex) => {
          return (
            <Row key={`ans-${answerIndex}`} className="mx-1">
              {answer.files.map((fileName, fileIndex) => {
                return (
                  <Col key={`answer-file-${fileIndex}`} className="px-1" xs={12 / answer.files.length}>
                    <OverlayTrigger
                      key={`${answerIndex}/${fileIndex}`}
                      placement="top"
                      overlay={
                        <Tooltip id={`tooltip-${answerIndex}-${fileIndex}`}>
                          {fileName}
                        </Tooltip>
                      }
                    >
                      <a href={urls[answerIndex][fileIndex]} key={`${answerIndex}/${fileIndex}`}>
                        <FaPaperclip size={20} className="text-purple ml-auto" />
                      </a>
                    </OverlayTrigger>
                  </Col>
                )
              })}
            </Row>
          )
        })}
      </Col>
    </Row>
  )
}

export default SingleQuestion;
