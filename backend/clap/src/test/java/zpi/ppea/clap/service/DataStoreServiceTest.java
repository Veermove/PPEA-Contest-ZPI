//package zpi.ppea.clap.service;
//
//import data_store.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.testcontainers.shaded.com.google.common.util.concurrent.ListenableFuture;
//import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
//import zpi.ppea.clap.repository.PartialRatingRepository;
//import zpi.ppea.clap.security.FirebaseAgent;
//
//import java.util.concurrent.ExecutionException;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class DataStoreServiceTest {
//
//    @Mock
//    private DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;
//
//    @InjectMocks
//    private DataStoreService dataStoreService;
//
//    @Test
//    void getUserClaimsTest() {
//        // given
//        FirebaseAgent.UserAuthData auth = new FirebaseAgent.UserAuthData(UserClaimsResponse.getDefaultInstance(), "false");
//
//        String email = "orlando.palladino@email.com";
//        UserClaimsResponse userClaim = UserClaimsResponse.newBuilder().setFirstName("Imię").setLastName("Nazwisko")
//                .setPersonId(11).setAssessorId(4).build();
//
//        com.google.common.util.concurrent.ListenableFuture<UserClaimsResponse> returned = ListenableFuture<>(userClaim);
//
//        when(dataStoreFutureStub.getUserClaims(UserRequest.newBuilder().setEmail(email).build()).thenReturn(returned);
//        //when
//        var newRating = dataStoreService.getUserClaims(auth, email);
//        //then
//        assertThat(newRating.getAssessorId()).isEqualTo(4);
//
//
//    }
//}
