package dev.rodrilang.tennis_tournaments.mappers;


import dev.rodrilang.tennis_tournaments.dtos.requests.ResultRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.responses.ResultResponseDto;
import dev.rodrilang.tennis_tournaments.entities.Result;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResultMapper {

    Result toEntity (ResultRequestDto resultRequestDto);

    ResultResponseDto toDto (Result result);

}
