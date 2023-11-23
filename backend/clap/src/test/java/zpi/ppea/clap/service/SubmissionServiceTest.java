package zpi.ppea.clap.service;

import data_store.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.logic.BusinessLogicService;
import zpi.ppea.clap.repository.PartialRatingRepository;
import zpi.ppea.clap.repository.SubmissionRepository;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private  SubmissionRepository submissionRepository;

    @InjectMocks
    private SubmissionService submissionService;

    @Test
    void getSubmissionsTest() {
        FirebaseAgent.UserAuthData auth = new FirebaseAgent.UserAuthData(UserClaimsResponse.getDefaultInstance(), "false");

        List<Assessor> asseList = new ArrayList<>();
        Assessor asse1 = Assessor.newBuilder().setAssessorId(1).setFirstName("Jan").setLastName("Kowalski")
                .build();
        Assessor asse2 = Assessor.newBuilder().setAssessorId(2).setFirstName("Jan").setLastName("Nowak")
                .build();
        asseList.add(asse1);
        asseList.add(asse2);

        List<Rating> ratList = new ArrayList<>();
        Rating rat1 = Rating.newBuilder().setRatingId(1).setAssessorId(1).setIsDraft(false)
                .setType(RatingType.INDIVIDUAL).build();
        Rating rat2 = Rating.newBuilder().setRatingId(2).setAssessorId(2).setIsDraft(false)
                .setType(RatingType.INDIVIDUAL).build();
        ratList.add(rat1);
        ratList.add(rat2);

        List<Submission> subList = new ArrayList<>();
        Submission sub1 = Submission.newBuilder().setSubmissionId(1).setYear(2022).setName("Rok 2022")
                .setDurationDays(90).addAllAssessors(asseList).addAllRatings(ratList).build();
        Submission sub2 = Submission.newBuilder().setSubmissionId(2).setYear(2023).setName("Rok 2023")
                .setDurationDays(120).addAllAssessors(asseList).addAllRatings(ratList).build();
        subList.add(sub1);
        subList.add(sub2);

        SubmissionsResponse subResponse = SubmissionsResponse.newBuilder().addAllSubmissions(subList).build();


        when(submissionRepository.allSubmissions(auth)).thenReturn(subResponse);
        //when
        var newSubList = submissionService.getSubmissions(auth);
        //then
        assertThat(newSubList.get(1).getAssessors().get(1).getLastName()).isEqualTo("Nowak");
    }

    @Test
    void getDetailedSubmissionTest() {
        FirebaseAgent.UserAuthData auth = new FirebaseAgent.UserAuthData(UserClaimsResponse.getDefaultInstance(), "false");

        int subId = 1;

        AppReport appRep = AppReport.newBuilder().setIsDraft(false).setReportSubmissionDate("2022-07-01")
                .setProjectGoals("goals.pdf").setOrganisationStructure("structure.pdf").setDivisionOfWork("division.pdf")
                .setProjectSchedule("schedule.pdf").setAttachments("att1.pdf, att2.pdf").build();

        DetailsSubmissionResponse detSubResponse = DetailsSubmissionResponse.newBuilder().setTeamSize(5)
                .setFinishDate("2022-08-01").setStatus(ProjectState.SUBMITTED).setBudget("12313.21").setDescription("op")
                .setReport(appRep).setPoints(95.12).build();

        when(submissionRepository.getDetailedSubmission(subId, auth)).thenReturn(detSubResponse);
        //when
        var newDetSubResponse = submissionService.getDetailedSubmission(subId, auth);
        //then
        assertThat(newDetSubResponse.getAppReportDto().getDivisionOfWork()).isEqualTo("division.pdf");
    }

}
