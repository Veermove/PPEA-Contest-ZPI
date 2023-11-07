package zpi.ppea.clap.logic;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import zpi.ppea.clap.annotations.ExclusiveFields;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;

public class ExclusiveFieldsValidator implements ConstraintValidator<ExclusiveFields, UpdatePartialRatingDto> {

    @Override
    public boolean isValid(UpdatePartialRatingDto dto, ConstraintValidatorContext context) {
        return (dto.getRatingId() == null) != (dto.getPartialRatingId() == null);
    }
}