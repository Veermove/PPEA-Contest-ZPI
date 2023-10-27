'use client'

import SubmissionDetails from "@/components/submission/details";
import { useAuthContext } from "@/context/authContext";
import { ClapApi } from "@/services/clap/api";
import { RatingType } from "@/services/clap/model/rating";
import { SubmissionDTO, SubmissionDetailsDTO } from "@/services/clap/model/submission";
import { redirect } from "next/navigation";
import { useEffect } from "react";

function Submission({ params }: { params: { submissionId: string } }) {
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

  const id = parseInt(params.submissionId)

  // const { data: submissions, error: errorA } = useSWR<SubmissionDTO[]>(() => clapApi.getSubmissions(0))
  // const { data: submissionDetails, error } = useSWR<SubmissionDetailsDTO>(() => clapApi.getSubmissionDetails(id))

  const submissions: SubmissionDTO[] = [
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
          type: RatingType.INDIVIDUAL,
          points: 10,
          isDraft: false,
          assessorId: 1
        }, {
          ratingId: 2,
          type: RatingType.INDIVIDUAL,
          points: 10,
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
      assesors: [
        {
          assesorId: 3,
          firstName: "Test",
          lastName: "Assessor",
          email: "test@assessor.com",
          personId: 3
        }, {
          assesorId: 4,
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
          points: 10,
          isDraft: false,
          assessorId: 4
        }, {
          ratingId: 5,
          type: RatingType.INITIAL,
          points: 10,
          isDraft: false,
          assessorId: 4
        }
      ]
    }
  ];

  const submissionDetails: SubmissionDetailsDTO = {
    durationDays: "20",
    name: "Test Submission Name",
    submissionId: 1,
    budget: "1 000 000 zł",
    finishDate: new Date(),
    description: `
    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac fermentum neque, id dapibus ante. Sed cursus congue tempor. Mauris non ante pharetra purus pulvinar sodales id ac sapien. Ut sed vulputate velit. Proin ac risus mi. Maecenas eleifend odio finibus, volutpat sem ut, gravida tortor. Duis dolor lacus, porta pretium ligula at, sodales vulputate ante. Ut suscipit tortor ut convallis varius. Donec porta nunc est, non hendrerit ligula sagittis sed. Nunc nec tempus est, non viverra elit. Phasellus mauris enim, congue ut purus nec, dignissim volutpat sapien. Cras feugiat rhoncus neque, non gravida est aliquet quis. Sed vel magna. 
    `,
    teamSize: 10,
    report: {
      applicationReportId: 123,
      projectGoals: 'https://www.google.com',
      organisationStructure: 'https://www.google.com',
      divisionOfWork: 'https://www.google.com',
      projectSchedule: 'https://www.google.com',
      attachements: 'https://www.google.com',
      isDraft: false,
      reportSubmissionDate: new Date()
    }
  }

  const submission = submissions?.find(submission => submission.submissionId === id)

  const error = false
  if (error || !submission || !submissionDetails) {
    // TODO error handling
    console.error(error)
  }
  return (
    <SubmissionDetails submissionDetails={submissionDetails!} submission={submission!} />
  );
}

export default Submission;
