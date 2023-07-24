package com.example.tabletennistournament.dtos;

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
}
