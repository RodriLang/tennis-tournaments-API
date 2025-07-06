package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.response.RoundResponseDto;
import dev.rodrilang.tennis_tournaments.models.Round;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MatchMapper.class)
public interface RoundMapper {

    @Mapping(source = "type", target = "round")
    RoundResponseDto toDto(Round round);

}
