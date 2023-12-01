package zpi.ppea.clap.repository;

import data_store.DataStoreGrpc;
import data_store.EmailRequest;
import data_store.EmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import zpi.ppea.clap.exceptions.NoAccessToResource;

import java.util.concurrent.ExecutionException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmailRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    @Retryable(retryFor = NoAccessToResource.class,
            maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public EmailResponse getEmailsToSent() throws NoAccessToResource {
        try {
            log.info("Trying to retrieve emails data.");
            return dataStoreFutureStub.getEmailDetails(EmailRequest.newBuilder().build()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e, "false");
        }
    }

    @Recover
    public EmailResponse recover(NoAccessToResource e) {
        log.error("Cannot retrieved data from datastore.");
        return null;
    }

}
