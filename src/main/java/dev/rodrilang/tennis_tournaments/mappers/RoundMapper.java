package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.models.Round;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MatchMapper.class)
public interface RoundMapper {

    RoundResponseDto toDto(Round round);

}
