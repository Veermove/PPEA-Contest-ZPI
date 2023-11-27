package zpi.ppea.clap.repository;

import data_store.DataStoreGrpc;
import data_store.PartialRating;
import data_store.PartialRatingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Repository;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.exceptions.RaceConditionException;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PartialRatingRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    public PartialRating upsertPartialRating(PartialRatingRequest partialRatingRequest, FirebaseAgent.UserAuthData authentication) {
        try {
            var response = dataStoreFutureStub.postPartialRating(partialRatingRequest).get();
            
            // We just detected race condition
            if (response.hasError())
                throw new RaceConditionException(null, authentication.getRefresh());
            // No races, valid partial rating received
            else
                return response.getPartialRating();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, authentication.getRefresh());
        }
    }
}
