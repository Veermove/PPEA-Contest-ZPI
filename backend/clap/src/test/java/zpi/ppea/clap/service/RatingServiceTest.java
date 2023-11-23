package zpi.ppea.clap.service;


import data_store.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zpi.ppea.clap.dtos.*;
import zpi.ppea.clap.dtos.RatingType;
import zpi.ppea.clap.repository.RatingsRepository;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingsRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void createNewRatingTest() {
        FirebaseAgent.UserAuthData auth = new FirebaseAgent.UserAuthData(UserClaimsResponse.getDefaultInstance(), "false");

        NewRatingDto newRatingDto = NewRatingDto.builder().ratingType(RatingType.INITIAL).build();
        Rating returned = Rating.newBuilder().setRatingId(1).setIsDraft(false)
                .setAssessorId(2).setType(data_store.RatingType.INITIAL).build();
        when(ratingRepository.createNewRating(1, auth, data_store.RatingType.INITIAL)).thenReturn(returned);
        //when
        var newRating = ratingService.createNewRating(1, newRatingDto, auth);
        //then
        assertThat(newRating.getAssessorId()).isEqualTo(2);
    }

    @Test
    void submitRatingDraftTest() {
        // given
        FirebaseAgent.UserAuthData auth = new FirebaseAgent.UserAuthData(UserClaimsResponse.getDefaultInstance(), "false");

        int draftId = 1;
        Rating returned = Rating.newBuilder().setRatingId(1).setIsDraft(true)
                .setAssessorId(2).setType(data_store.RatingType.INITIAL).build();
        when(ratingRepository.submitRatingDraft(draftId, auth)).thenReturn(returned);
        //when
        var newRating = ratingService.submitRatingDraft(draftId, auth);
        //then
        assertThat(newRating.isDraft()).isEqualTo(true);
    }

    @Test
    void getSubmissionRatingsTest() {
        // given
        FirebaseAgent.UserAuthData auth = new FirebaseAgent.UserAuthData(UserClaimsResponse.getDefaultInstance(), "false");

        int submissionId = 1;

        List<Criterion> criterionList = new ArrayList<>();
        Criterion criterion1 = Criterion.newBuilder().setCriterionId(1).setName("crit1").setDescription("opis")
                .setArea("area").setCriteria("crit1").setSubcriteria("1a").build();
        Criterion criterion2 = Criterion.newBuilder().setCriterionId(2).setName("crit2").setDescription("opis")
                .setArea("area").setCriteria("crit2").setSubcriteria("1b").build();
        criterionList.add(criterion1);
        criterionList.add(criterion2);

        List<PartialRating> partialRatingsList = new ArrayList<>();
        PartialRating rating1 = PartialRating.newBuilder().setPartialRatingId(1).setCriterionId(1).setPoints(10)
                .setJustification("uzasadnienie").setModified("kiedys").setModifiedBy(1).build();
        PartialRating rating2 = PartialRating.newBuilder().setPartialRatingId(2).setCriterionId(2).setPoints(15)
                .setJustification("uzasadnienie jakies").setModified("kiedys").setModifiedBy(1).build();
        partialRatingsList.add(rating1);
        partialRatingsList.add(rating2);

        List<AssessorRatings> assessorRatingsList = new ArrayList<>();

        AssessorRatings assessorRating = AssessorRatings.newBuilder().setAssessorId(1).setFirstName("Jan")
                .setLastName("Kowalski").setIsDraft(false).addAllPartialRatings(partialRatingsList).setRatingId(1)
                .build();
        assessorRatingsList.add(assessorRating);

        RatingsSubmissionResponse returned = RatingsSubmissionResponse.newBuilder().addAllCriteria(criterionList)
                .addAllIndividual(assessorRatingsList).build();
        when(ratingRepository.submissionRatings(submissionId, auth)).thenReturn(returned);
        //when
        var returnedRatings = ratingService.getSubmissionRatings(submissionId, auth);
        //then
        assertThat(returnedRatings.getCriteria().get(1).getCriteria()).isEqualTo("crit2");
    }


}
