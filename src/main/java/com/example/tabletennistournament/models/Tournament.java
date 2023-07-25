package com.example.tabletennistournament.models;

import com.example.tabletennistournament.enums.TournamentType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTime;
    private Integer userCount;
    @Enumerated(EnumType.STRING)
    private TournamentType tournamentType;
    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;
    @ManyToMany(mappedBy = "tournaments", cascade = CascadeType.PERSIST)
    private List<User> users;
    @OneToMany(mappedBy = "tournament")
    private List<Match> matches;
}
