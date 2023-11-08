package zpi.ppea.clap.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zpi.ppea.clap.dtos.PartialRatingDto;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.PartialRatingService;

@RestController
@RequestMapping("/partialratings")
@AllArgsConstructor
public class PartialRatingController {

    private final FirebaseAgent firebaseAgent;
    private final PartialRatingService partialRatingService;

    @PostMapping
    public ResponseEntity<PartialRatingDto> upsertPartialRating(
            @RequestHeader(name = "Authorization") String bearerToken,
            @Valid @RequestBody UpdatePartialRatingDto updatePartialRatingDto) {
        var authentication = firebaseAgent.authenticate(bearerToken);
        return ResponseEntity.ok()
                .header(FirebaseAgent.REFRESH_TOKEN_NAME, authentication.getRefresh())
                .body(partialRatingService.upsertPartialRating(updatePartialRatingDto, authentication));
    }

}
