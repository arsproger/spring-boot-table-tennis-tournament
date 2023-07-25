package com.example.tabletennistournament.models;

import com.example.tabletennistournament.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column(name = "full_name")
    private String fullName;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime registerDate;
    private Integer numberOfMatchesWon;
    private Integer numberOfTournamentsWon;
    @OneToMany(mappedBy = "creator")
    private List<Tournament> createTournaments;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "users_tournaments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tournament_id")
    )
    private List<Tournament> tournaments;
}
