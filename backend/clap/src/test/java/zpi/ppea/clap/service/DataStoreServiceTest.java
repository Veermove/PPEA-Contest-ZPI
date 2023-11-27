package zpi.ppea.clap.service;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import data_store.DataStoreGrpc;
import data_store.UserClaimsResponse;
import data_store.UserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.security.FirebaseAgent;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataStoreServiceTest {

    @Mock
    private DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    @InjectMocks
    private DataStoreService dataStoreService;

    @Test
    void getUserClaimsTest() {
        // given
        FirebaseAgent.UserAuthData auth = new FirebaseAgent.UserAuthData(UserClaimsResponse.getDefaultInstance(), "false");
        String email = "orlando.palladino@email.com";
        UserClaimsResponse userClaim = UserClaimsResponse.newBuilder().setFirstName("Imię").setLastName("Nazwisko")
                .setPersonId(11).setAssessorId(4).build();
        UserRequest userRequest = UserRequest.newBuilder().setEmail(email).build();
        when(dataStoreFutureStub.getUserClaims(userRequest)).thenReturn(Futures.immediateFuture(userClaim));

        //when
        var newRating = dataStoreService.getUserClaims(auth, email);

        //then
        assertThat(newRating.getAssessorId()).isEqualTo(4);
    }

    @Test
    void getUserClaimsTest_grpcError() {
        // given
        FirebaseAgent.UserAuthData auth = new FirebaseAgent.UserAuthData(UserClaimsResponse.getDefaultInstance(), "false");
        String email = "orlando.palladino@email.com";
        UserRequest userRequest = UserRequest.newBuilder().setEmail(email).build();

        SettableFuture<UserClaimsResponse> exceptionFuture = SettableFuture.create();
        exceptionFuture.setException(new InterruptedException());
        when(dataStoreFutureStub.getUserClaims(userRequest)).thenReturn(exceptionFuture);

        // when + then
        assertThrows(NoAccessToResource.class, () -> dataStoreService.getUserClaims(auth, email));
    }

}
