package com.example.tabletennistournament.mappers;

import com.example.tabletennistournament.dtos.TournamentDto;
import com.example.tabletennistournament.models.Tournament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TournamentMapper {
    Tournament map(TournamentDto dto);

    TournamentDto map(Tournament entity);

    List<TournamentDto> map(List<Tournament> entities);
}
