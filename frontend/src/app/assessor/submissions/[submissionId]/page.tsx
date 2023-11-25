'use client'

import ErrorComponent from "@/components/error";
import Spinner from "@/components/spinner";
import SubmissionDetails from "@/components/submission/details";
import { useAuthContext } from "@/context/authContext";
import { useClapAPI } from "@/context/clapApiContext";
import { SubmissionDTO, SubmissionDetailsDTO } from "@/services/clap/model/submission";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

function Submission({ params }: { params: { submissionId: string } }) {
  const { user } = useAuthContext()

  const id = parseInt(params.submissionId)
  useEffect(() => {
    if (Number.isNaN(id)) {
      redirect('/assessor/submissions');
    }
  })

  const [submission, setSubmission] = useState<SubmissionDTO | undefined>(undefined);
  const [submissionDetails, setSubmissionDetails] = useState<SubmissionDetailsDTO | undefined>(undefined);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  const { t } = useTranslation('submission/details')
  const clapApi = useClapAPI();

  useEffect(() => {
    if (!user || !clapApi) {
      return
    }
    (async () => {
      setLoading(true)
      const [submissions, submissionDetails] = await Promise.all([
        clapApi!.getSubmissions(),
        clapApi!.getSubmissionDetails(id)
      ])
      const currentSubmission = submissions.find(submission => submission.submissionId === id);
      if (!currentSubmission || !submissionDetails) {
        setError(true);
        return;
      }
      setSubmission(currentSubmission);
      setSubmissionDetails(submissionDetails);
    })().catch((error) => {
      console.error(error);
      setError(true);
    })
      .finally(() => {
        setLoading(false);
      })
  }, [clapApi, user, id])

  if (!user) {
    return redirect('/');
  } else if (loading) {
    return (
      <Spinner />
    )
  } else if (error) {
    return (
      <ErrorComponent text={t('error')} />
    )
  }
  return (
    <SubmissionDetails submissionDetails={submissionDetails!} submission={submission!} />
  );
}

export default Submission;
