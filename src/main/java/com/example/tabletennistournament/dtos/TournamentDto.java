package com.example.tabletennistournament.dtos;

import com.example.tabletennistournament.enums.TournamentType;
import com.example.tabletennistournament.models.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TournamentDto {
    private LocalDateTime dateTime;
    private TournamentType tournamentType;
    private List<User> users;
}
