package zpi.ppea.clap.repository;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import data_store.DataStoreGrpc;
import data_store.PartialRating;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.exceptions.RaceConditionException;
import zpi.ppea.clap.mappers.DtoMapper;
import zpi.ppea.clap.security.FirebaseAgent;

@Repository
@RequiredArgsConstructor
public class PartialRatingRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    public PartialRating upsertPartialRating(UpdatePartialRatingDto updatePartialRatingDto, FirebaseAgent.UserAuthData authentication) {
        try {
            updatePartialRatingDto.setModified(updatePartialRatingDto.getModified() == null ? "" : updatePartialRatingDto.getModified());
            var requestBuilder = DtoMapper.INSTANCE.dtoToPartialRatingRequest(updatePartialRatingDto).toBuilder();
            var request = requestBuilder.setAssessorId(authentication.getClaims().getAssessorId()).build();
            var response = dataStoreFutureStub.postPartialRating(request).get();
            
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
