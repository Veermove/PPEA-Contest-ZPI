package zpi.ppea.clap.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.PartialRatingDto;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.mappers.PartialRatingMapper;
import zpi.ppea.clap.repository.PartialRatingRepository;
import zpi.ppea.clap.security.FirebaseAgent;

@Service
@AllArgsConstructor
public class PartialRatingService {

    private final PartialRatingRepository partialRatingRepository;

    public PartialRatingDto upsertPartialRating(UpdatePartialRatingDto updatePartialRatingDto, FirebaseAgent.UserAuthData authentication) {
        var updatedRating = partialRatingRepository.upsertPartialRating(updatePartialRatingDto, authentication);
        return PartialRatingMapper.partialRatingToDto(updatedRating);
    }
}
