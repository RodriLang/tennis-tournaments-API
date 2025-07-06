package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.response.MatchResponseDto;
import dev.rodrilang.tennis_tournaments.models.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(target = "matchId", source = "id")
    MatchResponseDto toDto (Match match);

}
