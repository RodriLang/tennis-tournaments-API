package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.request.PlayerRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.PlayerResponseDto;
import dev.rodrilang.tennis_tournaments.models.Player;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    Player toEntity (PlayerRequestDto playerRequestDto);

    PlayerResponseDto toDto (Player player);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "dni", ignore = true)
    void updatePlayerFromDto(PlayerRequestDto dto, @MappingTarget Player player);

}
