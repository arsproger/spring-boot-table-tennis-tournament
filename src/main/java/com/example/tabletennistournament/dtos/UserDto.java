package com.example.tabletennistournament.dtos;

import com.example.tabletennistournament.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
    private String fullName;
    private Integer numberOfMatchesWon;
    private Integer numberOfTournamentsWon;
    private Integer numberOfMatchesPlayed;
    private Integer numberOfTournamentsPlayed;
    private Double matchWinningPercentage;
    private Double tournamentWinningPercentage;
    private UserRole role;
}
