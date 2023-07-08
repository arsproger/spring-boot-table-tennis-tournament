package com.example.tabletennistournament.models;


import com.example.tabletennistournament.enums.MatchType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matches")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String player1;
    private String player2;
    private Integer player1Score;
    private Integer player2Score;
    private String winner;
    private Long nextMatchId;
    private Integer nextMatchPlayerNumber;
    @Enumerated(EnumType.STRING)
    private MatchType matchType;
}
