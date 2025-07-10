package dev.rodrilang.tennis_tournaments.mappers;

import dev.rodrilang.tennis_tournaments.dtos.request.TournamentRequestDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentDetailDto;
import dev.rodrilang.tennis_tournaments.dtos.response.TournamentListDto;
import dev.rodrilang.tennis_tournaments.models.Tournament;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {RoundMapper.class, PlayerMapper.class})
public interface TournamentMapper {

    Tournament toEntity (TournamentRequestDto tournamentRequestDto);

    @Mapping(target = "tournamentId", source = "id")
    TournamentDetailDto toDetailDto(Tournament tournament);

    @Mapping(target = "numberOfPlayers", expression = "java(tournament.getPlayers().size())")
    TournamentListDto toListDto(Tournament tournament);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTournamentFromDto(TournamentRequestDto dto, @MappingTarget Tournament tournament);

}
