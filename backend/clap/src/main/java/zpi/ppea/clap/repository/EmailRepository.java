package zpi.ppea.clap.repository;

import data_store.*;
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
    public EmailResponse recoverEmailSending(NoAccessToResource e) {
        log.error("Cannot retrieved data from datastore.");
        return null;
    }

    @Retryable(retryFor = NoAccessToResource.class,
            maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public ConfirmationResponse sendConfirmation(ConfirmationRequest confirmationRequest) {
        try {
            log.info("Trying to send confirmation");
            return dataStoreFutureStub.confirmEmailsSent(confirmationRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e, "false");
        }
    }

    @Recover
    public ConfirmationResponse recoverConfirmationSending(NoAccessToResource e) {
        log.error("Cannot send confirmation.");
        return null;
    }

}
