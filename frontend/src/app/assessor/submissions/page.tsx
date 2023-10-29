'use client'

import SubmissionList from "@/components/submission/list";
import { useAuthContext } from "@/context/authContext";
import { ClapApi } from "@/services/clap/api";
import { RatingType } from "@/services/clap/model/rating";
import { SubmissionDTO } from "@/services/clap/model/submission";
import { useLocalStorage } from "@uidotdev/usehooks";
import { redirect } from "next/navigation";
import { useEffect } from "react";

function Submissions() {
  const { user } = useAuthContext()
  let clapApi: ClapApi;

  if (!user) {
    return redirect('/');
  }

  useEffect(() => {
    (async () => {
      clapApi = new ClapApi(await user.getIdToken())
    })
  })

  const [submissions, setSubmissions] = useLocalStorage<SubmissionDTO[]>("submissions", []);

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
      assessors: [
        {
          assessorId: 1,
          firstName: "Test",
          lastName: "Assessor",
          email: "test@assessor.com",
          personId: 1
        }, {
          assessorId: 2,
          firstName: "Another",
          lastName: "Assessor",
          email: "test@assessor.com",
          personId: 2
        }
      ],
      ratings: [
        {
          ratingId: 1,
          type: RatingType.INDIVIDUAL,
          isDraft: false,
          assessorId: 1
        }, {
          ratingId: 2,
          type: RatingType.INDIVIDUAL,
          isDraft: false,
          assessorId: 2
        }
      ]
    }, {
      durationDays: "25",
      name: "Really Long Long Long Long Test Submission Name",
      contest: {
        contestId: 2,
        year: 2020
      },
      submissionId: 2,
      assessors: [
        {
          assessorId: 3,
          firstName: "Test",
          lastName: "Assessor",
          email: "test@assessor.com",
          personId: 3
        }, {
          assessorId: 4,
          firstName: "Another",
          lastName: "Assessor",
          email: "test@assessor.com",
          personId: 4
        }
      ],
      ratings: [
        {
          ratingId: 6,
          type: RatingType.FINAL,
          isDraft: false,
          assessorId: 4
        }, {
          ratingId: 5,
          type: RatingType.INITIAL,
          isDraft: false,
          assessorId: 4
        }
      ]
    }
  ];

  if (error || !submissionList || !submissionList.length) {
    // TODO error handling
    console.error(error)
  }

  setSubmissions(submissionList);

  return (
    <>
      <SubmissionList submissionList={submissionList} />
    </>
  );
}

export default Submissions;
