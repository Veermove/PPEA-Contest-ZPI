package zpi.ppea.clap.service;

import data_store.DataStoreGrpc;
import data_store.UserClaimsResponse;
import data_store.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DataStoreService {
    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    @SneakyThrows
    public UserClaimsResponse getUserClaims(FirebaseAgent.UserAuthData data, String email) {
        try {
            return dataStoreFutureStub.getUserClaims(UserRequest.newBuilder().setEmail(email).build()).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, data.getRefresh());
        }
    }

}
