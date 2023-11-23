package zpi.ppea.clap.repository;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import data_store.DataStoreGrpc;
import data_store.PartialRating;
import data_store.PartialRatingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.exceptions.RaceConditionException;
import zpi.ppea.clap.mappers.DtoMapper;
import zpi.ppea.clap.security.FirebaseAgent;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PartialRatingRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    public PartialRating upsertPartialRating(UpdatePartialRatingDto updatePartialRatingDto, FirebaseAgent.UserAuthData authentication) {
        try {
            DtoMapper mapper = DtoMapper.INSTANCE;
            PartialRatingRequest.Builder builder = mapper.dtoToPartialRatingRequest(updatePartialRatingDto).toBuilder();
            mapper.setAssessorId(builder, authentication);

            PartialRatingRequest request = builder.build();
            log.info("Upserting partial rating:\n {}", request);
            var response = dataStoreFutureStub.postPartialRating(request).get();

            // We just detected race condition
            if (response.hasError()) {
                throw new RaceConditionException(null, authentication.getRefresh());
            }
            return response.getPartialRating();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, authentication.getRefresh());
        }
    }
}
