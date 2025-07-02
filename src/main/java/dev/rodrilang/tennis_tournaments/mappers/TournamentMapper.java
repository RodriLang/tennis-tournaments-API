package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentResponseDto;
import dev.rodrilang.tennis_tournaments.models.Tournament;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TournamentMapper {

    Tournament toEntity (TournamentRequestDto tournamentRequestDto);

    TournamentResponseDto toDto (Tournament tournament);

}
