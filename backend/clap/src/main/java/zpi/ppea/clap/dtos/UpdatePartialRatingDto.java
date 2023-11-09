package zpi.ppea.clap.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
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
    @NotEmpty
    @Length(min = 10, max = 250)
    String justification;

    @NotNull
    @Positive
    @Max(100)
    Integer points;
}
