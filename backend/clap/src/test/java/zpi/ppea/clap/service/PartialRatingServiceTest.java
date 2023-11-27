package zpi.ppea.clap.service;

import data_store.PartialRating;
import data_store.PartialRatingRequest;
import data_store.UserClaimsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.repository.PartialRatingRepository;
import zpi.ppea.clap.security.FirebaseAgent;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartialRatingServiceTest {
    
    @Mock
    private PartialRatingRepository partialRatingRepository;

    @InjectMocks
    private PartialRatingService partialRatingService;

    @Test
    void upsertPartialRating() {
        // given
        FirebaseAgent.UserAuthData auth = new FirebaseAgent.UserAuthData(UserClaimsResponse.getDefaultInstance(), "false");
        PartialRatingRequest partialRatingRequest = PartialRatingRequest.newBuilder()
                .setPartialRatingId(1).setCriterionId(1).setJustification("ok").setPoints(5).build();
        UpdatePartialRatingDto partialRatingDto = UpdatePartialRatingDto.builder()
                .partialRatingId(1).criterionId(1).justification("ok").points(5).modified("").build();
        PartialRating partialRating = PartialRating.newBuilder()
                .setPartialRatingId(1).setModifiedBy(1).setJustification("ok").setCriterionId(1).setPoints(5).build();
        when(partialRatingRepository.upsertPartialRating(partialRatingRequest, auth)).thenReturn(partialRating);
        
        // when
        var newPartialRating = partialRatingService.upsertPartialRating(partialRatingDto, auth);
        
        // then
        assertThat(newPartialRating.getPartialRatingId()).isEqualTo(1);
    }
    
}
