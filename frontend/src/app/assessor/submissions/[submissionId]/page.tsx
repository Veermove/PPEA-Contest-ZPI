'use client'

import Error from "@/components/error";
import Spinner from "@/components/spinner";
import SubmissionDetails from "@/components/submission/details";
import { useAuthContext } from "@/context/authContext";
import { ClapApi } from "@/services/clap/api";
import { SubmissionDTO, SubmissionDetailsDTO } from "@/services/clap/model/submission";
import { useLocalStorage } from "@uidotdev/usehooks";
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

  const [submissions] = useLocalStorage<SubmissionDTO[]>("submissions", []);
  const submission = submissions?.find(submission => submission.submissionId === id)
  useEffect(() => {
    if (!submission) {
      return redirect('/assessor/submissions');
    }
  })

  const [submissionDetails, setSubmissionDetails] = useState<SubmissionDetailsDTO | undefined>(undefined);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  const { t } = useTranslation('submission/details')

  useEffect(() => {
    if (!user) {
      return
    }
    (async () => {
      setLoading(true)
      const clapApi = new ClapApi(await user.getIdToken());
      clapApi.getSubmissionDetails(id).then((submissionDetails) => {
        setSubmissionDetails(submissionDetails);
      })
        .catch((error) => {
          console.error(error);
          setError(true);
        })
        .finally(() => {
          setLoading(false);
        })
    })()
  }, [user, id])

  if (!user) {
    return redirect('/');
  } else if (loading) {
    return (
      <Spinner />
    )
  } else if (error) {
    return (
      <Error text={t('error')} />
    )
  }
  return (
    <SubmissionDetails submissionDetails={submissionDetails!} submission={submission!} />
  );
}

export default Submission;
