'use client'

import { useTranslation } from "@/app/i18n/client";
import Error from "@/components/error";
import SubmissionList from "@/components/submission/list";
import { useAuthContext } from "@/context/authContext";
import { ClapApi } from "@/services/clap/api";
import { SubmissionDTO } from "@/services/clap/model/submission";
import { useLocalStorage } from "@uidotdev/usehooks";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";

function Submissions() {
  const { user } = useAuthContext()
  const { t } = useTranslation('submissions/page')
  let clapApi: ClapApi;

  if (!user) {
    return redirect('/');
  }

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [_, setSubmissionsLocalStorage] = useLocalStorage<SubmissionDTO[]>("submissions", []);
  const [submissionList, setSubmissionList] = useState<SubmissionDTO[]>([]);

  useEffect(() => {
    (async () => {
      setLoading(true);
      if (!clapApi) {
        clapApi = new ClapApi(await user.getIdToken());
      }
      clapApi.getSubmissions().then((submissions) => {
        setSubmissionList(submissions);
        setSubmissionsLocalStorage(submissions);
      })
        .catch((error) => {
          console.error(error);
          setError(true);
        })
        .finally(() => {
          setLoading(false);
        })
    })()
  }, [])

  if (loading) {
    return (
      <div className="d-flex justify-content-center mt-3">
        <div className="spinner-border text-purple" role="status">
        </div>
      </div>
    )
  } else if (error || !submissionList.length) {
    return Error({ text: t('noSubmissions') })
  } else {
    return (
      <>
        <SubmissionList submissionList={submissionList} />
      </>
    );
  }
}

export default Submissions;
