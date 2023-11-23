package zpi.ppea.clap.logic;

import data_store.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessLogicServiceTest {

    @Mock
    private DataStoreGrpc.DataStoreBlockingStub dataStoreBlockingStub;

    @InjectMocks
    private BusinessLogicService businessLogicService;

    @Test
    void calculateSubmissionRatingTest() {
        int subId = 1;
        int asseId = 1;

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

        RatingsSubmissionRequest req = RatingsSubmissionRequest.newBuilder().setSubmissionId(subId).setAssessorId(asseId)
                .build();

        when(dataStoreBlockingStub.getSubmissionRatings(req)).thenReturn(returned);
        //when
        var calculatedRaiting = businessLogicService.calculateSubmissionRating(subId, asseId);
        //then
        assertThat(calculatedRaiting).isEqualTo(12.5);
    }
}
