import Spinner from "@/components/spinner";
import { Question } from "@/services/clap/model/studyVisits";
import { getFileLink } from "@/services/firebase/attachments/getAttachment";
import { useEffect, useState } from "react";
import { FaPaperclip } from "react-icons/fa";

function SingleQuestion({question, submissionId}: {question: Question, submissionId: number}) {

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
    <tr>
      <td>{content}</td>
      <td>{answers.map(answer => answer.answerText).join('\n')}</td>
      <td>{answers.map((answer, answerIndex) => {
        return answer.files.map((_, fileIndex) => {
          return (
            <a href={urls[answerIndex][fileIndex]} key={`${answerIndex}/${fileIndex}`}>
              <FaPaperclip size={10} className="text-purple ml-auto" />
            </a>
          )
        })
      })}</td>
    </tr>
  )
}

export default SingleQuestion;
