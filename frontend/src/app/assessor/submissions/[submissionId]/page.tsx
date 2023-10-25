'use client'

import SubmissionDetails from "@/components/submission/submissionDetails";
import { useAuthContext } from "@/context/authContext";
import { ClapApi } from "@/services/clap/api";
import { SubmissionDTO, SubmissionDetailsDTO } from "@/services/clap/model/submission";
import { redirect } from "next/navigation";
import { useEffect } from "react";
import useSWR from 'swr';

function Submission({ submissionId, submission }: { submissionId: number, submission: SubmissionDTO }) {
    const { user } = useAuthContext()
    let clapApi: ClapApi;

    if (!user) {
        return redirect('/');
      }
    
    useEffect(() => {
        (async () => {
            clapApi = new ClapApi( await user.getIdToken())
        })
    })

    const { data: submissionDetails, error } = useSWR<SubmissionDetailsDTO>(() => clapApi.getSubmissionDetails(submissionId))
  
    if (error || !submissionDetails) {
      // TODO error handling
      console.error(error)
    }
    return (
        <SubmissionDetails submissionDetails={submissionDetails!} submission={submission} />
    );
}

export default Submission;
