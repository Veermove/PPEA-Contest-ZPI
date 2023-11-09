package zpi.ppea.clap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.logic.BusinessLogicService;
import zpi.ppea.clap.mappers.DtoMapper;
import zpi.ppea.clap.repository.SubmissionRepository;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {


    private final BusinessLogicService businessLogicService;
    private final SubmissionRepository submissionRepository;

    @Cacheable("Submissions")
    public List<SubmissionDto> getSubmissions(FirebaseAgent.UserAuthData authentication) {
        return DtoMapper.INSTANCE.submissionListToDtos(submissionRepository.allSubmissions(authentication).getSubmissionsList());
    }

    @Cacheable("DetailSubmission")
    public DetailsSubmissionResponseDto getDetailedSubmission(Integer submissionId, FirebaseAgent.UserAuthData authentication) {
        var detailsSubmissionResponseDto = DtoMapper.INSTANCE.detailsSubmissionToDto(
                submissionRepository.getDetailedSubmission(submissionId, authentication));
        detailsSubmissionResponseDto.setPoints(businessLogicService.calculateSubmissionRating(
                submissionId, authentication.getClaims().getAssessorId()));
        return detailsSubmissionResponseDto;
    }

}
