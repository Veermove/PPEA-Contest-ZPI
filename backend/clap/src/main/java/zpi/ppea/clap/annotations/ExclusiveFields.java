package zpi.ppea.clap.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import zpi.ppea.clap.logic.ExclusiveFieldsValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExclusiveFieldsValidator.class)
public @interface ExclusiveFields {
    String message() default "Exactly one of the fields should be set (ratingId or partialRatingId).";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
