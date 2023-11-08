package zpi.ppea.clap.service;

import data_store.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.RatingsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.logic.BusinessLogicService;
import zpi.ppea.clap.mappers.DtoMapper;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DataStoreService {
    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    private final BusinessLogicService businessLogicService;

    @SneakyThrows
    public UserClaimsResponse getUserClaims(FirebaseAgent.UserAuthData data, String email) {
        try {
            return dataStoreFutureStub.getUserClaims(
                    UserRequest.newBuilder()
                            .setEmail(email)
                            .build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e, data.getRefresh());
        }
    }


    public List<SubmissionDto> getSubmissions(FirebaseAgent.UserAuthData data) {
        var allSubmissionsGrpc = dataStoreFutureStub.getSubmissions(
                SubmissionRequest.newBuilder()
                        .setAssessorId(data.getClaims().getAssessorId())
                        .build()
        );

        try {
            return DtoMapper.INSTANCE.submissionListToDtos(allSubmissionsGrpc.get().getSubmissionsList());
        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e, data.getRefresh());
        }
    }

    public DetailsSubmissionResponseDto getDetailedSubmission(FirebaseAgent.UserAuthData data, Integer submissionId) {
        var detailsSubmissionResponse = dataStoreFutureStub.getSubmissionDetails(DetailsSubmissionRequest.newBuilder()
                .setSubmissionId(submissionId)
                .setAssessorId(data.getClaims().getAssessorId())
                .build()
        );

        DetailsSubmissionResponse response;
        try {
            response = detailsSubmissionResponse.get();

        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e, data.getRefresh());
        }

        var dto = DtoMapper.INSTANCE.detailsSubmissionToDto(response);
        var ratings = getSubmissionRatingsBase(data.getRefresh(), submissionId, data.getClaims().getAssessorId());
        //dto.setPoints(businessLogicService.calculateSubmissionRating(ratings, submissionId, data.getClaims().getAssessorId()));
        return dto;
    }


    public RatingsSubmissionResponseDto getSubmissionRatings(FirebaseAgent.UserAuthData data, Integer submissionId) {
        var resp = getSubmissionRatingsBase(data.getRefresh(), submissionId, data.getClaims().getAssessorId());
        return RatingsSubmissionResponseDto.builder()
                .criteria(DtoMapper.INSTANCE.criterionListToDto(resp.getCriteriaList()))
                .individualRatings(DtoMapper.INSTANCE.assessorRatingsListToDtos(resp.getIndividualList()))
                .initialRating(DtoMapper.INSTANCE.assessorRatingsToDtos(resp.getInitial()))
                .finalRating(DtoMapper.INSTANCE.assessorRatingsToDtos(resp.getInitial()))
                .build();
    }

    public RatingsSubmissionResponse getSubmissionRatingsBase(String refresh, Integer submissionId, Integer assessorId) {
        var ft = dataStoreFutureStub.getSubmissionRatings(RatingsSubmissionRequest.newBuilder()
                .setAssessorId(assessorId)
                .setSubmissionId(submissionId)
                .build()
        );

        RatingsSubmissionResponse resp;

        try {
            resp = ft.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e, refresh);
        }

        return resp;
    }

}
