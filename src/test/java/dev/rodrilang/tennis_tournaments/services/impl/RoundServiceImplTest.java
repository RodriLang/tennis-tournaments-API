package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.enums.RoundType;
import dev.rodrilang.tennis_tournaments.exceptions.InvalidTournamentStatusException;
import dev.rodrilang.tennis_tournaments.models.*;
import dev.rodrilang.tennis_tournaments.services.MatchService;
import dev.rodrilang.tennis_tournaments.strategy.RoundFactory;
import dev.rodrilang.tennis_tournaments.strategy.RoundStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoundServiceImplTest {

    @InjectMocks
    private RoundServiceImpl roundService;

    @Mock
    private MatchService matchService;

    @Mock
    private RoundFactory roundFactory;

    @Mock
    private RoundStrategy roundStrategy;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void generateNextRound_shouldReturnNextRound_whenAllMatchesCompleted() {
        // Arrange
        Player p1 = Player.builder().id(1L).name("Juan").build();
        Player p2 = Player.builder().id(2L).name("Pedro").build();
        Match match = Match.builder().id(1L).playerOne(p1).playerTwo(p2).build();

        Round currentRound = Round.builder()
                .type(RoundType.SEMI_FINAL)
                .matches(List.of(match))
                .build();

        // Mock: todos los partidos tienen ganador
        when(matchService.thereIsAWinner(match)).thenReturn(true);
        when(matchService.getWinner(match)).thenReturn(p1);

        // Mock: estrategia para siguiente ronda
        when(roundFactory.getStrategy(RoundType.FINAL)).thenReturn(roundStrategy);
        when(roundStrategy.generateMatches(anyList(), any(Round.class)))
                .thenReturn(List.of(
                        Match.builder().playerOne(p1).playerTwo(p2).build()
                ));

        // Act
        Round nextRound = roundService.generateNextRound(currentRound);

        // Assert
        assertNotNull(nextRound);
        assertEquals(RoundType.FINAL, nextRound.getType());
        assertEquals(1, nextRound.getMatches().size());
        verify(roundFactory).getStrategy(RoundType.FINAL);
        verify(roundStrategy).generateMatches(anyList(), any(Round.class));
    }

    @Test
    void generateNextRound_shouldThrowException_whenCurrentRoundIsFinal() {
        Round finalRound = Round.builder()
                .type(RoundType.FINAL)
                .matches(List.of())
                .build();

        InvalidTournamentStatusException ex = assertThrows(
                InvalidTournamentStatusException.class,
                () -> roundService.generateNextRound(finalRound)
        );

        assertEquals("Cannot generate next round after FINAL", ex.getMessage());
    }

    @Test
    void generateNextRound_shouldThrowException_whenMatchIncomplete() {
        Match match = Match.builder().id(1L).build();

        Round currentRound = Round.builder()
                .type(RoundType.SEMI_FINAL)
                .matches(List.of(match))
                .build();

        when(matchService.thereIsAWinner(match)).thenReturn(false);

        InvalidTournamentStatusException ex = assertThrows(
                InvalidTournamentStatusException.class,
                () -> roundService.generateNextRound(currentRound)
        );

        assertTrue(ex.getMessage().contains("Match ID"));
    }
}