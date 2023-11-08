package zpi.ppea.clap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.logic.BusinessLogicService;
import zpi.ppea.clap.mappers.DetailedSubmissionMapper;
import zpi.ppea.clap.mappers.SubmissionMapper;
import zpi.ppea.clap.repository.SubmissionRepository;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {


    private final BusinessLogicService businessLogicService;
    private final SubmissionRepository submissionRepository;

    @Cacheable("submissions")
    public List<SubmissionDto> getSubmissions(FirebaseAgent.UserAuthData authentication) {
        return SubmissionMapper.submissionListToDtos(submissionRepository.allSubmissions(authentication).getSubmissionsList());
    }

    @Cacheable("detailSubmission")
    public DetailsSubmissionResponseDto getDetailedSubmission(Integer submissionId, FirebaseAgent.UserAuthData authentication) {
        var detailsSubmissionResponseDto = DetailedSubmissionMapper.mapToDto(
                submissionRepository.getDetailedSubmission(submissionId, authentication));
        detailsSubmissionResponseDto.setPoints(businessLogicService.calculateSubmissionRating(submissionId));
        return detailsSubmissionResponseDto;
    }

}
