package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.exceptions.InvalidResultException;
import dev.rodrilang.tennis_tournaments.models.SetScore;
import dev.rodrilang.tennis_tournaments.utils.SetScoreUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SetScoreValidatorImplTest {

    private SetScoreValidatorImpl validator;

    @BeforeEach
    void setUp() {
        validator = new SetScoreValidatorImpl();
    }

    @Test
    void isValidSetScore_shouldReturnTrueForValidScore() {
        try (MockedStatic<SetScoreUtils> utilities = org.mockito.Mockito.mockStatic(SetScoreUtils.class)) {
            utilities.when(() -> SetScoreUtils.validateFullScore(6, 4)).thenReturn(true);
            assertTrue(validator.isValidSetScore(6, 4));
        }
    }

    @Test
    void isValidSetScore_shouldReturnFalseForInvalidScore() {
        try (MockedStatic<SetScoreUtils> utilities = org.mockito.Mockito.mockStatic(SetScoreUtils.class)) {
            utilities.when(() -> SetScoreUtils.validateFullScore(8, 2)).thenReturn(false);
            assertFalse(validator.isValidSetScore(8, 2));
        }
    }

    @Test
    void isValidPartialScore_shouldReturnTrueForValidPartial() {
        try (MockedStatic<SetScoreUtils> utilities = org.mockito.Mockito.mockStatic(SetScoreUtils.class)) {
            utilities.when(() -> SetScoreUtils.validatePartialScore(4)).thenReturn(true);
            assertTrue(validator.isValidPartialScore(4));
        }
    }

    @Test
    void isValidPartialScore_shouldReturnFalseForInvalidPartial() {
        try (MockedStatic<SetScoreUtils> utilities = org.mockito.Mockito.mockStatic(SetScoreUtils.class)) {
            utilities.when(() -> SetScoreUtils.validatePartialScore(-1)).thenReturn(false);
            assertFalse(validator.isValidPartialScore(-1));
        }
    }

    @Test
    void validateSetScore_shouldPassForValidScore() {
        SetScore setScore = new SetScore();
        setScore.setPlayerOneScore(6);
        setScore.setPlayerTwoScore(4);

        try (MockedStatic<SetScoreUtils> utilities = org.mockito.Mockito.mockStatic(SetScoreUtils.class)) {
            utilities.when(() -> SetScoreUtils.validateFullScore(6, 4)).thenReturn(true);

            assertDoesNotThrow(() -> validator.validateSetScore(setScore));
        }
    }

    @Test
    void validateSetScore_shouldThrowForInvalidScore() {
        SetScore setScore = new SetScore();
        setScore.setPlayerOneScore(9);
        setScore.setPlayerTwoScore(2);

        try (MockedStatic<SetScoreUtils> utilities = org.mockito.Mockito.mockStatic(SetScoreUtils.class)) {
            utilities.when(() -> SetScoreUtils.validateFullScore(9, 2)).thenReturn(false);

            assertThrows(InvalidResultException.class, () -> validator.validateSetScore(setScore));
        }
    }
}
