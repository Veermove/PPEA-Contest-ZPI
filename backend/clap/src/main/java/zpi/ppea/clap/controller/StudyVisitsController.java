package zpi.ppea.clap.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zpi.ppea.clap.config.ValueConfig;
import zpi.ppea.clap.dtos.StudyVisitDto;
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.StudyVisitService;

@RestController
@RequestMapping("/visits")
@AllArgsConstructor
public class StudyVisitsController {

    private final ValueConfig valueConfig;
    private final FirebaseAgent firebaseAgent;
    private final StudyVisitService studyVisitService;

    @GetMapping("/{submissionId}")
    public ResponseEntity<StudyVisitDto> getStudyVisits(
            @PathVariable Integer submissionId,
            @RequestHeader(name = "Authorization") String bearerToken) {
        var authentication = firebaseAgent.authenticate(bearerToken);
        return ResponseEntity.ok()
                .header(valueConfig.getRefreshTokenHeaderName(), authentication.getRefresh())
                .body(studyVisitService.getStudyVisits(submissionId, authentication));
    }

}
