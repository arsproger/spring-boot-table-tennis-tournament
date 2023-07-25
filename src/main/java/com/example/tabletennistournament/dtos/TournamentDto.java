package com.example.tabletennistournament.dtos;

import com.example.tabletennistournament.enums.TournamentType;
import com.example.tabletennistournament.models.User;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TournamentDto {
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTime;
    private TournamentType tournamentType;
    private Integer userCount;
    private List<User> users;
    private User creator;
}
