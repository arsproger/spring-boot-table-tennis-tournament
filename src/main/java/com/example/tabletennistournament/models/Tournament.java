package com.example.tabletennistournament.models;

import com.example.tabletennistournament.enums.TournamentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tournaments")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    private TournamentType tournamentType;
    @ManyToMany(mappedBy = "tournaments")
    private List<User> users;
    @OneToMany(mappedBy = "tournament")
    private List<Match> matches;
}
