package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.requests.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.TournamentResponseDto;
import dev.rodrilang.tennis_tournaments.entities.Tournament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    Tournament toEntity (TournamentRequestDto tournamentRequestDto);

    TournamentResponseDto toDto (Tournament tournament);

}
