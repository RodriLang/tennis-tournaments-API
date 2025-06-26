package dev.rodrilang.tennis_tournaments.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import dev.rodrilang.tennis_tournaments.validations.impl.ResultSetCountValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ResultSetCountValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidResultSetCount {
    String message() default "Solo se permiten hasta 3 sets por partido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
