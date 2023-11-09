'use client'

import { useTranslation } from "@/app/i18n/client";
import Error from "@/components/error";
import Ratings from "@/components/ratings/ratings";
import Spinner from "@/components/spinner";
import { useAuthContext } from "@/context/authContext";
import { useClapAPI } from "@/context/clapApiContext";
import { RatingType, RatingsDTO } from "@/services/clap/model/rating";
import { redirect } from "next/navigation";
import { useEffect, useState } from "react";
import { isSubmissionRatingActive } from "../ratingsFilter";

function IndividualRatings() {
  const { t } = useTranslation('ratings/individual');
  const clapApi = useClapAPI();

  const [loading, setLoading] = useState<boolean>(true);
  const [ratings, setRatings] = useState<RatingsDTO>();

  const { user } = useAuthContext();
  const [assessorId, setAssessorId] = useState<number>(0);

  useEffect(() => {
    (async () => {
      setAssessorId((await user!.getIdTokenResult()).claims.assessorId as number)
    })()
  }, [user])

  useEffect(() => {
    if (!clapApi) {
      return;
    }
    (async () => {
      setLoading(true);
      const submissions = await clapApi!.getSubmissions();
      const currentYear = new Date().getFullYear();
      const currentSubmission = submissions.find(s => s.year === currentYear)
      if (!currentSubmission) {
        setLoading(false);
        return;
      }

      let submissionRatings = await clapApi!.getSubmissionRatings(currentSubmission.submissionId)
      if (!isSubmissionRatingActive(submissionRatings, RatingType.INDIVIDUAL)) {
        setLoading(false);
        return;
      }

      const assessorRatings = submissionRatings.individualRatings.find(rating => rating.assessorId === assessorId);
      if (!assessorRatings) {
        await clapApi!.createRating(currentSubmission.submissionId, {ratingType: RatingType.INDIVIDUAL});
        submissionRatings = await clapApi!.getSubmissionRatings(currentSubmission.submissionId)
      }

      setRatings(submissionRatings);
      setLoading(false);
    })()
  })

  if (loading) {
    return Spinner;
  } else if (!user) {
    return redirect('/login')
  } else if (!ratings) {
    return <Error text={t('noRatings')} />
  } else {
    return <Ratings ratings={ratings.individualRatings} criteria={ratings.criteria} type={RatingType.INDIVIDUAL} />
  }
}

export default IndividualRatings
