import Spinner from "@/components/spinner";
import { Question } from "@/services/clap/model/studyVisits";
import { getFileLink } from "@/services/firebase/attachments/getAttachment";
import { useEffect, useState } from "react";
import { Col, Row } from "react-bootstrap";
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
      <Col xs={4}>
        {content}
      </Col>
      <Col xs={6}>
        {answers.map((answer, idx) => {
          return (<Row key={`answer-${idx}`} className="mt-2">{answer.answerText}
          </Row>)
        })}
      </Col>
      <Col xs={2}>
        {answers.map((answer, answerIndex) => {
          return (
            <Row key={`ans-${answerIndex}`}>
              {answer.files.map((_, fileIndex) => {
                return (
                  <Col key={`answer-file-${fileIndex}`}>
                    <a href={urls[answerIndex][fileIndex]} key={`${answerIndex}/${fileIndex}`}>
                      <FaPaperclip size={20} className="text-purple ml-auto" />
                    </a>
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
