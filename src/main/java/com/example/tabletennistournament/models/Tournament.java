package com.example.tabletennistournament.models;

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
    private String name;
//    @OneToMany(mappedBy = "tournament")
    private List<String> users;
    @OneToMany(mappedBy = "tournament")
    private List<Match> matches;
}
