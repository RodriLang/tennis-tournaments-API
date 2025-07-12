
package dev.rodrilang.tennis_tournaments.services.impl;

import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentDetailDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentListDto;
import dev.rodrilang.tennis_tournaments.mappers.TournamentMapper;
import dev.rodrilang.tennis_tournaments.models.Tournament;
import dev.rodrilang.tennis_tournaments.repositories.TournamentRepository;
import dev.rodrilang.tennis_tournaments.utils.TestDataFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceImplTest {

    @Mock private TournamentRepository tournamentRepository;
    @Mock private TournamentMapper tournamentMapper;

    @InjectMocks private TournamentServiceImpl tournamentService;

    private Tournament tournament;
    private TournamentRequestDto tournamentRequestDto;
    private TournamentDetailDto tournamentDetailDto;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        tournament = TestDataFactory.buildTournament();
        tournamentRequestDto = TestDataFactory.buildTournamentRequestDto();
        tournamentDetailDto = TestDataFactory.buildTournamentDetailDto();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void create_shouldReturnDetailDto() {
        when(tournamentMapper.toEntity(tournamentRequestDto)).thenReturn(tournament);
        when(tournamentRepository.save(tournament)).thenReturn(tournament);
        when(tournamentMapper.toDetailDto(tournament)).thenReturn(tournamentDetailDto);

        TournamentDetailDto response = tournamentService.create(tournamentRequestDto);

        assertEquals(tournamentDetailDto, response);
    }

    @Test
    void findById_shouldReturnDetailDto() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(tournamentMapper.toDetailDto(tournament)).thenReturn(tournamentDetailDto);

        TournamentDetailDto result = tournamentService.findById(1L);

        assertEquals(tournamentDetailDto, result);
    }

    @Test
    void delete_shouldCallRepository() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        tournamentService.delete(1L);
        verify(tournamentRepository).delete(tournament);
    }

    @Test
    void getAll_shouldReturnList() {
        when(tournamentRepository.findAll()).thenReturn(List.of(tournament));
        when(tournamentMapper.toListDto(tournament)).thenReturn(TestDataFactory.buildTournamentListDto());
        List<TournamentListDto> result = tournamentService.getAll();
        assertEquals(1, result.size());
    }

    // Para completar: más tests para registrar jugadores, avanzar rondas, asignar resultados, etc.
}
