package com.example.tabletennistournament.models;

import com.example.tabletennistournament.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

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
    private LocalDateTime registerDate;
    private Integer numberOfMatchesWon;
    private Integer numberOfTournamentsWon;
    @ManyToMany
    @JoinTable(
            name = "user_tournament",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tournament_id")
    )
    private List<Tournament> tournaments;
}
