package zpi.ppea.clap.service;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import data_store.DataStoreGrpc;
import data_store.UserClaimsResponse;
import data_store.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.security.FirebaseAgent;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataStoreService {
    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    @SneakyThrows
    public UserClaimsResponse getUserClaims(FirebaseAgent.UserAuthData data, String email) {
        try {
            log.info("Getting user claims for user with email {}", email);
            return dataStoreFutureStub.getUserClaims(UserRequest.newBuilder().setEmail(email).build()).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, data.getRefresh());
        }
    }

}
