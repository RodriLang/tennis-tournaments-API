package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.enums.StatusType;
import dev.rodrilang.tennis_tournaments.exceptions.IncompleteMatchException;
import dev.rodrilang.tennis_tournaments.exceptions.InvalidTournamentStatusException;
import dev.rodrilang.tennis_tournaments.exceptions.MatchNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.MatchMapper;
import dev.rodrilang.tennis_tournaments.mappers.ResultMapper;
import dev.rodrilang.tennis_tournaments.mappers.PlayerMapper;
import dev.rodrilang.tennis_tournaments.models.Match;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.models.Result;
import dev.rodrilang.tennis_tournaments.repositories.MatchRepository;
import dev.rodrilang.tennis_tournaments.services.ResultService;
import dev.rodrilang.tennis_tournaments.utils.TestDataFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private MatchMapper matchMapper;
    @Mock
    private ResultMapper resultMapper;
    @Mock
    private ResultService resultService;
    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Match match;
    private Player playerOne;
    private Player playerTwo;
    private Result result;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        playerOne = Player.builder().id(1L).dni("123").build();
        playerTwo = Player.builder().id(2L).dni("456").build();
        result = new Result();
        match = Match.builder()
                .id(1L)
                .playerOne(playerOne)
                .playerTwo(playerTwo)
                .result(null)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findMatchById_shouldReturnMatchDto() {

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(matchMapper.toDto(match)).thenReturn(TestDataFactory.buildMatchResponseDto());

        assertEquals(TestDataFactory.buildMatchResponseDto(), matchService.findMatchById(1L));
    }

    @Test
    void findMatchById_shouldThrowExceptionIfNotFound() {
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(MatchNotFoundException.class, () -> matchService.findMatchById(1L));
    }

    @Test
    void addResultToMatch_shouldThrowIfAlreadyHasResult() {
        match.setResult(new Result());
        assertThrows(InvalidTournamentStatusException.class,
                () -> matchService.addResultToMatch(match, TestDataFactory.buildResultRequestDto())
        );
    }

    @Test
    void addResultToMatch_shouldSaveAndReturnDto() {

        Result entity = new Result();
        Match saved = Match.builder().id(1L).status(StatusType.FINISHED).build();
        MatchResponseDto expectedDto = TestDataFactory.buildMatchResponseDto();

        when(resultMapper.toEntity(TestDataFactory.buildResultRequestDto())).thenReturn(entity);
        match.setResult(entity);
        when(resultService.getSetsWonPlayerOne(entity)).thenReturn(2);
        when(matchRepository.save(any())).thenReturn(saved);
        when(matchMapper.toDto(saved)).thenReturn(expectedDto);

        MatchResponseDto response = matchService.updateResult(match, TestDataFactory.buildResultRequestDto());
        assertEquals(expectedDto, response);
    }

    @Test
    void getMatchesByPlayer_shouldReturnMatchList() {
        when(matchRepository.getMatchesByPlayerOne_DniOrPlayerTwo_Dni("123", "123"))
                .thenReturn(List.of(match));
        when(matchMapper.toDto(match)).thenReturn(TestDataFactory.buildMatchResponseDto());

        List<MatchResponseDto> response = matchService.getMatchesByPlayer("123");
        assertEquals(1, response.size());
    }

    @Test
    void getWinner_shouldReturnPlayerOne() {
        match.setResult(result);
        when(resultService.getSetsWonPlayerOne(result)).thenReturn(2);
        assertEquals(playerOne, matchService.getWinner(match));
    }

    @Test
    void getWinner_shouldReturnPlayerTwo() {
        match.setResult(result);
        when(resultService.getSetsWonPlayerOne(result)).thenReturn(0);
        when(resultService.getSetsWonPlayerTwo(result)).thenReturn(2);
        assertEquals(playerTwo, matchService.getWinner(match));
    }

    @Test
    void getWinner_shouldThrowIfNoResult() {
        assertThrows(IncompleteMatchException.class, () -> matchService.getWinner(match));
    }

    @Test
    void getWinner_shouldThrowIfNoWinner() {
        match.setResult(result);
        when(resultService.getSetsWonPlayerOne(result)).thenReturn(1);
        when(resultService.getSetsWonPlayerTwo(result)).thenReturn(1);

        assertThrows(IncompleteMatchException.class, () -> matchService.getWinner(match));
    }

    @Test
    void thereIsAWinner_shouldReturnTrueIfPlayerOneWins() {
        match.setResult(result);
        when(resultService.getSetsWonPlayerOne(result)).thenReturn(2);
        assertTrue(matchService.thereIsAWinner(match));
    }

    @Test
    void thereIsAWinner_shouldReturnTrueIfPlayerTwoWins() {
        match.setResult(result);
        when(resultService.getSetsWonPlayerOne(result)).thenReturn(0);
        when(resultService.getSetsWonPlayerTwo(result)).thenReturn(2);
        assertTrue(matchService.thereIsAWinner(match));
    }

    @Test
    void thereIsAWinner_shouldReturnFalseIfNoWinner() {
        match.setResult(result);
        when(resultService.getSetsWonPlayerOne(result)).thenReturn(1);
        when(resultService.getSetsWonPlayerTwo(result)).thenReturn(1);
        assertFalse(matchService.thereIsAWinner(match));
    }

    @Test
    void getWinnerById_shouldReturnPlayerDto() {
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        match.setResult(result);
        when(resultService.getSetsWonPlayerOne(result)).thenReturn(2);
        when(playerMapper.toDto(playerOne)).thenReturn(TestDataFactory.buildPlayerOneDto());

        assertNotNull(matchService.getWinner(1L));
    }
}
