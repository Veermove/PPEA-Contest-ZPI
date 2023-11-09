package zpi.ppea.clap.repository;

import data_store.DataStoreGrpc;
import data_store.PartialRating;
import data_store.PartialRatingRequest;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Repository;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.mappers.DtoMapper;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class PartialRatingRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    public PartialRating upsertPartialRating(UpdatePartialRatingDto updatePartialRatingDto, FirebaseAgent.UserAuthData authentication) {
        try {
            PartialRatingRequest request = DtoMapper.INSTANCE.dtoToPartialRatingRequest(
                    updatePartialRatingDto, authentication.getClaims().getAssessorId());

            return dataStoreFutureStub.postPartialRating(request).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, authentication.getRefresh());
        }
    }
}
