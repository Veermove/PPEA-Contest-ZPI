package zpi.ppea.clap.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.StudyVisitDto;
import zpi.ppea.clap.mappers.DtoMapper;
import zpi.ppea.clap.repository.StudyVisitRepository;
import zpi.ppea.clap.security.FirebaseAgent;

@Service
@AllArgsConstructor
public class StudyVisitService {

    private final StudyVisitRepository studyVisitRepository;

    @Cacheable("Visits")
    public StudyVisitDto getStudyVisits(Integer submissionId, FirebaseAgent.UserAuthData authentication) {
        return DtoMapper.INSTANCE.toStudyVisitDto(studyVisitRepository.getStudyVisits(submissionId, authentication));
    }
}
