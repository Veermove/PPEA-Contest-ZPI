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

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DataStoreService {
    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    @SneakyThrows
    public UserClaimsResponse getUserClaims(String email) {
        try {
            return dataStoreFutureStub.getUserClaims(
                UserRequest.newBuilder()
                    .setEmail(email)
                    .build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e.getMessage());
        }
    }


    public List<SubmissionDto> getSubmissions(Integer assessorId) {
        var allSubmissionsGrpc = dataStoreFutureStub.getSubmissions(
            SubmissionRequest.newBuilder()
                .setAssessorId(assessorId)
                .build()
        );

        try {
            return DtoMapper.INSTANCE.submissionListToDtos(allSubmissionsGrpc.get().getSubmissionsList());
        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e.getMessage());
        }
    }

    public DetailsSubmissionResponseDto getDetailedSubmission(Integer submissionId, Integer assessorId) {
        var detailsSubmissionResponse = dataStoreFutureStub.getSubmissionDetails(DetailsSubmissionRequest.newBuilder()
            .setSubmissionId(submissionId)
            .setAssessorId(assessorId)
            .build()
        );

        DetailsSubmissionResponse response;
        try {
            response = detailsSubmissionResponse.get();

        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e.getMessage());
        }

        var dto = DtoMapper.INSTANCE.detailsSubmissionToDto(response);
        var ratings = getSubmissionRatingsBase(submissionId, assessorId);
        dto.setPoints(BusinessLogicService.calculateSubmissionRating(ratings, submissionId, assessorId));
        return dto;
    }


    public RatingsSubmissionResponseDto getSubmissionRatings(Integer submissionId, Integer assessorId) {
        var resp = getSubmissionRatingsBase(submissionId, assessorId);
        return RatingsSubmissionResponseDto.builder()
            .criteria(DtoMapper.INSTANCE.criterionListToDto(resp.getCriteriaList()))
            .individualRatings(DtoMapper.INSTANCE.assessorRatingsListToDtos(resp.getIndividualList()))
            .initialRating(DtoMapper.INSTANCE.assessorRatingsToDtos(resp.getInitial()))
            .finalRating(DtoMapper.INSTANCE.assessorRatingsToDtos(resp.getInitial()))
            .build();
    }

    public RatingsSubmissionResponse getSubmissionRatingsBase(Integer submissionId, Integer assessorId) {
        var ft = dataStoreFutureStub.getSubmissionRatings(RatingsSubmissionRequest.newBuilder()
            .setAssessorId(assessorId)
            .setSubmissionId(submissionId)
            .build()
        );

        RatingsSubmissionResponse resp;

        try {
            resp = ft.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new NoAccessToResource(e.getMessage());
        }

        return resp;
    }

}
