package dev.rodrilang.tennis_tournaments.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import dev.rodrilang.tennis_tournaments.validations.impl.DateRangeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "La fecha de inicio no puede ser anterior a la fecha de finalización";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}