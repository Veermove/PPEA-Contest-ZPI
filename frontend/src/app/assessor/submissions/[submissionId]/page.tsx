'use client'

import SubmissionDetails from "@/components/submission/details";
import { useAuthContext } from "@/context/authContext";
import { ClapApi } from "@/services/clap/api";
import { SubmissionDTO, SubmissionDetailsDTO } from "@/services/clap/model/submission";
import { redirect } from "next/navigation";
import { useEffect } from "react";
import useSWR from 'swr';

function Submission({ params }: { params: {submissionId: string} }) {
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

    const id = parseInt(params.submissionId)

    const { data: submissions, error: errorA } = useSWR<SubmissionDTO[]>(() => clapApi.getSubmissions(0))
    const { data: submissionDetails, error } = useSWR<SubmissionDetailsDTO>(() => clapApi.getSubmissionDetails(id))

    const submission = submissions?.find(submission => submission.submissionId === id)
  
    if (error || !submissionDetails) {
      // TODO error handling
      console.error(error)
    }
    return (
        <SubmissionDetails submissionDetails={submissionDetails!} submission={submission!} />
    );
}

export default Submission;
