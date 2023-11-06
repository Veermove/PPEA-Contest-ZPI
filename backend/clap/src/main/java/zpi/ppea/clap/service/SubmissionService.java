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

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {


    private final BusinessLogicService businessLogicService;
    private final AuthenticationService authenticationService;
    private final SubmissionRepository submissionRepository;

    @Cacheable("submissions")
    public List<SubmissionDto> getSubmissions() {
        return SubmissionMapper.submissionListToDtos(submissionRepository.allSubmissions().getSubmissionsList());
    }

    @Cacheable("detailSubmission")
    public DetailsSubmissionResponseDto getDetailedSubmission(Integer submissionId) {
        authenticationService.checkAccessToResource(submissionId);
        var detailsSubmissionResponseDto = DetailedSubmissionMapper.mapToDto(submissionRepository.getDetailedSubmission(submissionId));
        detailsSubmissionResponseDto.setPoints(businessLogicService.calculateSubmissionRating(submissionId));
        return detailsSubmissionResponseDto;
    }

}
