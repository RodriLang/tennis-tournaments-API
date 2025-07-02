package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.SetScoreDto;
import dev.rodrilang.tennis_tournaments.models.SetScore;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SetScoreMapper {

    SetScore toEntity (SetScoreDto setScoreDto);

    SetScoreDto toDto (SetScore setScore);

}
