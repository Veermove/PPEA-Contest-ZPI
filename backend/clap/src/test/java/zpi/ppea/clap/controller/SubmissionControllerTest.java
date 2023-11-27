package zpi.ppea.clap.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import zpi.ppea.clap.config.IntegrationTestConfig;
import zpi.ppea.clap.config.ValidData;
import zpi.ppea.clap.logic.BusinessLogicService;
import zpi.ppea.clap.repository.StudyVisitRepository;
import zpi.ppea.clap.repository.SubmissionRepository;
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.StudyVisitService;
import zpi.ppea.clap.service.SubmissionService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SubmissionController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {IntegrationTestConfig.class, SubmissionController.class,
        SubmissionService.class, StudyVisitService.class
})
class SubmissionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SubmissionRepository submissionRepository;

    @MockBean
    StudyVisitRepository studyVisitRepository;

    @MockBean
    BusinessLogicService businessLogicService;

    @MockBean
    FirebaseAgent firebaseAgent;

    @Test
    void getSubmissions_response200() {
        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        given(submissionRepository.allSubmissions(ValidData.ASSESSOR_AUTH))
                .willReturn(ValidData.SUBMISSIONS_RESPONSE);

        try {
            // when
            mockMvc.perform(get("/submissions")
                            .header(ValidData.AUTH_HEADER, ValidData.VALID_TOKEN))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON.toString()))
                    .andExpect(jsonPath("$.[0].submissionId").value(1))
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getDetailedSubmission_response200() {
        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        when(businessLogicService.calculateSubmissionRating(1, 1)).thenReturn(55.5);
        given(submissionRepository.getDetailedSubmission(1, ValidData.ASSESSOR_AUTH))
                .willReturn(ValidData.DETAILS_SUBMISSION_RESPONSE);

        try {
            // when
            mockMvc.perform(get("/submissions/1")
                            .header(ValidData.AUTH_HEADER, ValidData.VALID_TOKEN))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON.toString()))
                    .andExpect(jsonPath("$.description").value("New fast drone"))
                    .andExpect(jsonPath("$.points").value(55.5))
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getStudyVisits_response200() {
        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        given(studyVisitRepository.getStudyVisits(1, ValidData.ASSESSOR_AUTH))
                .willReturn(ValidData.STUDY_VISIT_RESPONSE);

        try {
            // when
            mockMvc.perform(get("/submissions/1/visits")
                            .header(ValidData.AUTH_HEADER, ValidData.VALID_TOKEN))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON.toString()))
                    .andExpect(jsonPath("$.questions[0].content").value("Was the project made with the newest technology?"))
                    .andExpect(jsonPath("$.questions[0].answers[0].answerText").value("No, there are some old libraries"))
                    .andExpect(jsonPath("$.questions[0].answers[1].answerText").value("No, definitely no"))
                    .andExpect(jsonPath("$.questions[1].content").value("Was the project well documented?"))
                    .andExpect(jsonPath("$.questions[1].answers[0].answerText").value("Yes, there are necessary files"))
                    .andExpect(jsonPath("$.questions[1].answers[0].files[0]").value("file1"))
                    .andExpect(jsonPath("$.questions[1].answers[0].files[1]").value("file2"))
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
