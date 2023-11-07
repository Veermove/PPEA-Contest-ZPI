package zpi.ppea.clap.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.PartialRatingDto;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.mappers.PartialRatingMapper;
import zpi.ppea.clap.repository.PartialRatingRepository;

@Service
@AllArgsConstructor
public class PartialRatingService {

    private final PartialRatingRepository partialRatingRepository;

    public PartialRatingDto upsertPartialRating(UpdatePartialRatingDto updatePartialRatingDto) {
        var updatedRating = partialRatingRepository.upsertPartialRating(updatePartialRatingDto);
        return PartialRatingMapper.partialRatingToDto(updatedRating);
    }
}
