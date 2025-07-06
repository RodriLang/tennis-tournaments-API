package dev.rodrilang.tennis_tournaments.mappers;


import dev.rodrilang.tennis_tournaments.dtos.request.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.ResultResponseDto;
import dev.rodrilang.tennis_tournaments.models.Result;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SetScoreMapper.class)
public interface ResultMapper {

    Result toEntity (ResultRequestDto resultRequestDto);

    ResultResponseDto toDto (Result result);

}
