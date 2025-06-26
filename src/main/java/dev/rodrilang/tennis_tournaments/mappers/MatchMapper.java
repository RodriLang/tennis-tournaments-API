package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.responses.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.entities.Match;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    MatchResponseDto toDto (Match match);

}
