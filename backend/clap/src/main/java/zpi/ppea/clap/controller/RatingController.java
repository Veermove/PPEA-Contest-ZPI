package zpi.ppea.clap.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zpi.ppea.clap.config.ValueConfig;
import zpi.ppea.clap.dtos.NewRatingDto;
import zpi.ppea.clap.dtos.PartialRatingDto;
import zpi.ppea.clap.dtos.RatingDto;
import zpi.ppea.clap.dtos.RatingsSubmissionResponseDto;
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.RatingService;

@RestController
@RequestMapping("/ratings")
@AllArgsConstructor
public class RatingController {

    private final ValueConfig valueConfig;
    private final FirebaseAgent firebaseAgent;
    private final RatingService ratingService;

    @GetMapping("/{submissionId}")
    public ResponseEntity<RatingsSubmissionResponseDto> getSubmissionRatings(
            @RequestHeader(name = "Authorization") String bearerToken,
            @PathVariable Integer submissionId) {
        var authentication = firebaseAgent.authenticate(bearerToken);
        return ResponseEntity.ok()
                .header(valueConfig.getRefreshTokenHeaderName(), authentication.getRefresh())
                .body(ratingService.getSubmissionRatings(submissionId, authentication));
    }

    @PostMapping("/{submissionId}")
    public ResponseEntity<RatingDto> createNewRatingForSubmission(
            @RequestHeader(name = "Authorization") String bearerToken,
            @PathVariable Integer submissionId,
            @Valid @RequestBody NewRatingDto newRatingDto) {
        var authentication = firebaseAgent.authenticate(bearerToken);
        return ResponseEntity.ok()
                .header(valueConfig.getRefreshTokenHeaderName(), authentication.getRefresh())
                .body(ratingService.createNewRating(submissionId, newRatingDto, authentication));
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<PartialRatingDto> submitRatingDraft(
            @RequestHeader(name = "Authorization") String bearerToken,
            @PathVariable Integer ratingId) {
        var authentication = firebaseAgent.authenticate(bearerToken);
        return ResponseEntity.ok()
                .header(valueConfig.getRefreshTokenHeaderName(), authentication.getRefresh())
                .body(ratingService.submitRatingDraft(ratingId, authentication));
    }

}
