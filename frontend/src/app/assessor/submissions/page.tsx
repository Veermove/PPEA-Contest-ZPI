'use client'

import SubmissionList from "@/components/submission/list";
import { useAuthContext } from "@/context/authContext";
import { ClapApi } from "@/services/clap/api";
import { RatingType } from "@/services/clap/model/rating";
import { SubmissionDTO } from "@/services/clap/model/submission";
import { redirect } from "next/navigation";
import { useEffect } from "react";

function Submissions() {
  const { user, userData } = useAuthContext()
  let clapApi: ClapApi;

  if (!user || !userData?.assessorId) {
      return redirect('/');
    }
    
  useEffect(() => {
    (async () => {
      clapApi = new ClapApi( await user.getIdToken())
    })
  })

  // const { data: submissionList, error } = useSWR<SubmissionDTO[]>(() => clapApi.getSubmissions(userData?.assessorId!))
  const error = false;

  const submissionList: SubmissionDTO[] = [
    {
      durationDays: "20",
      name: "Test Submission Name",
      contest: {
          contestId: 1,
          year: 2023
      },
      submissionId: 1,
      assesors: [
          {
            assesorId: 1,
            firstName: "Test",
            lastName: "Assessor",
            email: "test@assessor.com",
            personId: 1
          }, {
            assesorId: 2,
            firstName: "Another",
            lastName: "Assessor",
            email: "test@assessor.com",
            personId: 2
          }
        ],
      ratings: [
        {
          ratingId: 1,
          type: RatingType.FINAL,
          points: 10,
          isDraft: true,
          assessorId: 1
        }, {
          ratingId: 2,
          type: RatingType.INITIAL,
          points: 10,
          isDraft: false,
          assessorId: 2
        }
      ]
    }
  ];


  if (error || !submissionList || !submissionList.length) {
    // TODO error handling
    console.error(error)
  }
  return (
    <>
      <SubmissionList submissionList={submissionList} />
    </>
  );
}

export default Submissions;
