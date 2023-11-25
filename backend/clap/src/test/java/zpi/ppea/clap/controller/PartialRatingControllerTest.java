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
import zpi.ppea.clap.repository.PartialRatingRepository;
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.PartialRatingService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PartialRatingController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {IntegrationTestConfig.class, PartialRatingController.class, PartialRatingService.class})
class PartialRatingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PartialRatingRepository partialRatingRepository;

    @MockBean
    FirebaseAgent firebaseAgent;

    @Test
    void upsertPartialRating_response200() {
        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        given(partialRatingRepository.upsertPartialRating(ValidData.UPDATE_PARTIAL_RATING_DTO, ValidData.ASSESSOR_AUTH))
                .willReturn(ValidData.PARTIAL_RATING);

        try {
            // when
            mockMvc.perform(post("/partialratings")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .header(ValidData.AUTH_HEADER, ValidData.VALID_TOKEN)
                            .content("""
                                    {
                                        "partialRatingId": 1,
                                        "points": 100,
                                        "justification": "Very good work.",
                                        "criterionId": 1
                                    }
                                    """
                            ))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON.toString()))
                    .andExpect(jsonPath("$.partialRatingId").value(1))
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void upsertPartialRating_response400_RatingAndPartialRatingIdProvided() {
        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        given(partialRatingRepository.upsertPartialRating(ValidData.UPDATE_PARTIAL_RATING_DTO, ValidData.ASSESSOR_AUTH))
                .willReturn(ValidData.PARTIAL_RATING);

        try {
            // when
            mockMvc.perform(post("/partialratings")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .header(ValidData.AUTH_HEADER, ValidData.VALID_TOKEN)
                            .content("""
                                    {
                                        "partialRatingId": 1,
                                        "ratingId": 2,
                                        "points": 75,
                                        "justification": "Not bad.",
                                        "criterionId": 1
                                    }
                                    """
                            ))
                    // then
                    .andExpect(status().isBadRequest())
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}