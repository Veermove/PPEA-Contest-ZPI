package zpi.ppea.clap.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zpi.ppea.clap.dtos.PartialRatingDto;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.service.PartialRatingService;

@RestController
@RequestMapping("/partialratings")
@AllArgsConstructor
public class PartialRatingController {

    private final PartialRatingService partialRatingService;

    @PostMapping
    public ResponseEntity<PartialRatingDto> upsertPartialRating(@Valid @RequestBody UpdatePartialRatingDto updatePartialRatingDto) {
        return ResponseEntity.ok(partialRatingService.upsertPartialRating(updatePartialRatingDto));
    }

}
