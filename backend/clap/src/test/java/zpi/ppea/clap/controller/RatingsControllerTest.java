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
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.RatingService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(RatingController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {IntegrationTestConfig.class, RatingController.class})
class RatingsControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RatingService ratingService;

    @MockBean
    FirebaseAgent firebaseAgent;

    @Test
    void getRatings() {

        // given
        when(firebaseAgent.authenticate(ValidData.VALID_TOKEN)).thenReturn(ValidData.ASSESSOR_AUTH);
        given(ratingService.getSubmissionRatings(1, ValidData.ASSESSOR_AUTH))
                .willReturn(ValidData.ratingsSubmissionResponse);

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

}
