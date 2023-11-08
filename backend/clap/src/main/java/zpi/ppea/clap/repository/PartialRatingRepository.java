package zpi.ppea.clap.repository;

import data_store.DataStoreGrpc;
import data_store.PartialRating;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Repository;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.exceptions.GrpcConcurrentException;
import zpi.ppea.clap.mappers.PartialRatingMapper;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class PartialRatingRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    public PartialRating upsertPartialRating(UpdatePartialRatingDto updatePartialRatingDto, FirebaseAgent.UserAuthData authentication) {
        try {
            return dataStoreFutureStub.postPartialRating(PartialRatingMapper.dtoToPartialRatingRequest(
                    updatePartialRatingDto, authentication.getClaims().getAssessorId())).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new GrpcConcurrentException("Error while receiving data from datastore");
        }
    }
}
