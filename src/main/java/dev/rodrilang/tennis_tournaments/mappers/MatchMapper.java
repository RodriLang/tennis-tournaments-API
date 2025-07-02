package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.models.Match;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    MatchResponseDto toDto (Match match);

}
