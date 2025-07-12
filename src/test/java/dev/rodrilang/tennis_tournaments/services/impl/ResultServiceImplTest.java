package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.SetScoreDto;
import dev.rodrilang.tennis_tournaments.dtos.response.ResultResponseDto;
import dev.rodrilang.tennis_tournaments.exceptions.ResultNotFoundException;
import dev.rodrilang.tennis_tournaments.mappers.ResultMapper;
import dev.rodrilang.tennis_tournaments.mappers.SetScoreMapper;
import dev.rodrilang.tennis_tournaments.models.Result;
import dev.rodrilang.tennis_tournaments.models.SetScore;
import dev.rodrilang.tennis_tournaments.repositories.ResultRepository;
import dev.rodrilang.tennis_tournaments.services.SetScoreValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultServiceImplTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private ResultMapper resultMapper;

    @Mock
    private SetScoreMapper setScoreMapper;

    @Mock
    private SetScoreValidator setScoreValidator;

    @InjectMocks
    private ResultServiceImpl resultService;

    private Result resultEntity;
    private SetScoreDto setScoreDto;
    private SetScore setScoreEntity;
    private ResultResponseDto resultResponseDto;

    @BeforeEach
    void setup() {
        // Setup objetos ejemplo
        setScoreDto = new SetScoreDto(6, 4); // Ajusta constructor según tu DTO
        setScoreEntity = new SetScore();
        setScoreEntity.setPlayerOneScore(6);
        setScoreEntity.setPlayerTwoScore(4);

        resultEntity = new Result();
        resultEntity.setId(1L);
        resultEntity.setSetsScore(new java.util.ArrayList<>());

        resultResponseDto = new ResultResponseDto(Collections.emptyList());    }

    @Test
    void addSetScore_whenResultExists_shouldAddSetScoreAndReturnDto() {
        when(resultRepository.findById(1L)).thenReturn(Optional.of(resultEntity));
        when(setScoreMapper.toEntity(setScoreDto)).thenReturn(setScoreEntity);
        doNothing().when(setScoreValidator).validateSetScore(setScoreEntity);
        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(resultMapper.toDto(any(Result.class))).thenReturn(resultResponseDto);

        ResultResponseDto response = resultService.addSetScore(1L, setScoreDto);

        // Verifica que el setScore se haya agregado
        assertTrue(resultEntity.getSetsScore().contains(setScoreEntity));
        // Verifica que se haya llamado a validar y a guardar
        verify(setScoreValidator).validateSetScore(setScoreEntity);
        verify(resultRepository).save(resultEntity);
        verify(resultMapper).toDto(resultEntity);
        // El resultado devuelto es el esperado
        assertEquals(resultResponseDto, response);
    }

    @Test
    void addSetScore_whenResultNotFound_shouldThrowException() {
        when(resultRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResultNotFoundException.class, () -> resultService.addSetScore(1L, setScoreDto));
        verifyNoInteractions(setScoreValidator, setScoreMapper, resultMapper);
    }

    @Test
    void setFullResult_shouldSetAllSetsAndReturnDto() {
        List<SetScoreDto> dtoList = List.of(
                new SetScoreDto(6, 3),
                new SetScoreDto(4, 6)
        );

        List<SetScore> entityList = List.of(
                new SetScore(6, 3),
                new SetScore(4, 6)
        );

        when(resultRepository.findById(1L)).thenReturn(Optional.of(resultEntity));
        when(setScoreMapper.toEntity(dtoList.get(0))).thenReturn(entityList.get(0));
        when(setScoreMapper.toEntity(dtoList.get(1))).thenReturn(entityList.get(1));
        when(resultRepository.save(any(Result.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(resultMapper.toDto(any(Result.class))).thenReturn(resultResponseDto);

        ResultResponseDto response = resultService.setFullResult(1L, dtoList);

        assertEquals(entityList, resultEntity.getSetsScore());
        verify(resultRepository).save(resultEntity);
        verify(resultMapper).toDto(resultEntity);
        assertEquals(resultResponseDto, response);
    }

    @Test
    void setFullResult_whenResultNotFound_shouldThrowException() {
        when(resultRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResultNotFoundException.class, () -> resultService.setFullResult(1L, List.of(setScoreDto)));
        verifyNoInteractions(setScoreMapper, resultMapper);
    }

    @Test
    void getSetsWonPlayerOne_shouldReturnCountCorrectly() {
        SetScore set1 = new SetScore();
        set1.setPlayerOneScore(6);
        set1.setPlayerTwoScore(3);

        SetScore set2 = new SetScore();
        set2.setPlayerOneScore(4);
        set2.setPlayerTwoScore(6);

        SetScore set3 = new SetScore();
        set3.setPlayerOneScore(7);
        set3.setPlayerTwoScore(5);

        resultEntity.setSetsScore(List.of(set1, set2, set3));

        int setsWon = resultService.getSetsWonPlayerOne(resultEntity);

        assertEquals(2, setsWon);
    }

    @Test
    void getSetsWonPlayerTwo_shouldReturnCountCorrectly() {
        SetScore set1 = new SetScore();
        set1.setPlayerOneScore(6);
        set1.setPlayerTwoScore(3);

        SetScore set2 = new SetScore();
        set2.setPlayerOneScore(4);
        set2.setPlayerTwoScore(6);

        SetScore set3 = new SetScore();
        set3.setPlayerOneScore(3);
        set3.setPlayerTwoScore(7);

        resultEntity.setSetsScore(List.of(set1, set2, set3));

        int setsWon = resultService.getSetsWonPlayerTwo(resultEntity);

        assertEquals(2, setsWon);
    }

    @Test
    void findEntityById_whenNotFound_shouldThrowException() {
        when(resultRepository.findById(999L)).thenReturn(Optional.empty());

        // No es público, por lo que la prueba sería indirecta, pero por ejemplo se prueba con addSetScore o setFullResult
        assertThrows(ResultNotFoundException.class, () -> resultService.addSetScore(999L, setScoreDto));
    }
}
