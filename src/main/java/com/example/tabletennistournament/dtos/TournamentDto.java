package com.example.tabletennistournament.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TournamentDto {
    private LocalDateTime dateTime;
    private String name;
    private List<String> users;
}
