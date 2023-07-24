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
    @ManyToOne
    @JoinColumn(name = "player1_id", referencedColumnName = "id")
    private User player1;
    @ManyToOne
    @JoinColumn(name = "player2_id", referencedColumnName = "id")
    private User player2;
    private Integer player1Score;
    private Integer player2Score;
    @ManyToOne
    @JoinColumn(name = "winner_id", referencedColumnName = "id")
    private User winner;
    private Long nextMatchId;
    private Integer nextMatchPlayerNumber;
    @Enumerated(EnumType.STRING)
    private MatchType matchType;
    @ManyToOne
    @JoinColumn(name = "tournament_id", referencedColumnName = "id")
    private Tournament tournament;
}
