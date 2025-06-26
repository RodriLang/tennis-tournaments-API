package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.responses.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.entities.rounds.Round;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoundMapper {

    RoundResponseDto toDto (Round round);

}
