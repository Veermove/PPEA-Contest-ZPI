package zpi.ppea.clap.dtos;

import javax.annotation.Nonnegative;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zpi.ppea.clap.annotations.ExclusiveFields;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ExclusiveFields
public class UpdatePartialRatingDto {
    Integer criterionId;
    Integer ratingId;
    Integer partialRatingId;

    @NotNull
    @Length(max = 2500)
    String justification;

    @NotNull
    @Nonnegative
    @Max(100)
    Integer points;

    String modified;
}
