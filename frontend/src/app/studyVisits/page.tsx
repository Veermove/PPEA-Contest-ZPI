'use client'

import ErrorComponent from "@/components/error";
import Spinner from "@/components/spinner";
import SingleVisit from "@/components/submission/studyVisits/singleVisit";
import { useAuthContext } from "@/context/authContext";
import { useClapAPI } from "@/context/clapApiContext";
import { StudyVisit } from "@/services/clap/model/studyVisits";
import { SubmissionDTO } from "@/services/clap/model/submission";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";
import { useTranslation } from "../i18n/client";

function StudyVisits() {
  const { t } = useTranslation('studyVisits/page');
  const clapApi = useClapAPI();

  const [loading, setLoading] = useState<boolean>(true);
  const [submission, setSubmission] = useState<SubmissionDTO | undefined>(undefined);
  const [error, setError] = useState<boolean>(false);
  const [studyVisit, setStudyVisit] = useState<StudyVisit | undefined>(undefined);

  const { user } = useAuthContext();

  useEffect(() => {
    if (!clapApi || !user) {
      return;
    }
    (async () => {
      try {
        setLoading(true);
        const submissions = await clapApi!.getSubmissions();
        const currentYear = new Date().getFullYear();
        const currentSubmission = submissions.find(s => s.year === currentYear)
        if (!currentSubmission) {
          return;
        }
        setSubmission(currentSubmission);
        const studyVisit = await clapApi!.getStudyVisit(currentSubmission.submissionId);
        setStudyVisit(studyVisit);
      } catch {
        setError(true);
      } finally {
        setLoading(false);
      }
    })()
  }, [clapApi, user])

  if (loading) {
    return <Spinner />;
  } else if (!user) {
    return redirect('/')
  } else if (!submission || !studyVisit || error) {
    return <ErrorComponent text={t('noVisit')} />
  } else return (
    <SingleVisit
      questions={studyVisit?.questions}
      submissionId={submission.submissionId}
    />
  )
}

export default StudyVisits
