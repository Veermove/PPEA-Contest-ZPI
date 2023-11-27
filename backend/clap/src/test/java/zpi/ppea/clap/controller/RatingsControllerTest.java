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
import zpi.ppea.clap.repository.RatingsRepository;
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.RatingService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(RatingController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {IntegrationTestConfig.class, RatingController.class, RatingService.class})
class RatingsControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RatingsRepository ratingsRepository;

    @MockBean
    FirebaseAgent firebaseAgent;

    @Test
    void getSubmissionRatings_response200() {

        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        given(ratingsRepository.submissionRatings(1, ValidData.ASSESSOR_AUTH))
                .willReturn(ValidData.RATINGS_SUBMISSION_RESPONSE);

        try {
            // when
            mockMvc.perform(get("/ratings/1")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .header(ValidData.AUTH_HEADER, ValidData.VALID_TOKEN))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON.toString()))
                    .andExpect(jsonPath("$.finalRating.ratingId").value(1))
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createNewRatingForSubmission_response200() {
        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        given(ratingsRepository.createNewRating(1,ValidData.ASSESSOR_AUTH, data_store.RatingType.INITIAL))
                .willReturn(ValidData.RATING1);

        try {
            // when
            mockMvc.perform(post("/ratings/1")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .header(ValidData.AUTH_HEADER, ValidData.VALID_TOKEN)
                            .content("""
                                    {
                                        "ratingType": "INITIAL"
                                    }
                                    """)
                    )
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON.toString()))
                    .andExpect(jsonPath("$.ratingId").value(1))
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createNewRatingForSubmission_response400_invalidData() {
        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        given(ratingsRepository.createNewRating(1,ValidData.ASSESSOR_AUTH, data_store.RatingType.INITIAL))
                .willReturn(ValidData.RATING1);

        try {
            // when
            mockMvc.perform(post("/ratings/1")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .header(ValidData.AUTH_HEADER, ValidData.VALID_TOKEN)
                            .content("""
                                    {
                                        "ratingType": ""
                                    }
                                    """)
                    )
                    // then
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void submitRatingDraft_response200() {
        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        given(ratingsRepository.submitRatingDraft(1,ValidData.ASSESSOR_AUTH)).willReturn(ValidData.RATING1);

        try {
            // when
            mockMvc.perform(put("/ratings/1")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .header(ValidData.AUTH_HEADER, ValidData.VALID_TOKEN)
                    )
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON.toString()))
                    .andExpect(jsonPath("$.ratingId").value(1))
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
