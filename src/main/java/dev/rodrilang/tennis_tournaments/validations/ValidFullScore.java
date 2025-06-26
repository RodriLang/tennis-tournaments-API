package dev.rodrilang.tennis_tournaments.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import dev.rodrilang.tennis_tournaments.validations.impl.FullScoreValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FullScoreValidator.class)
@Target({ ElementType.TYPE }) // porque validaremos un objeto completo (playerOneScore + playerTwoScore)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFullScore {
    String message() default "Invalid full score";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}