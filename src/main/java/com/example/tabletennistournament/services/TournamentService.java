package com.example.tabletennistournament.services;

import com.example.tabletennistournament.enums.TournamentType;
import com.example.tabletennistournament.exceptions.AppException;
import com.example.tabletennistournament.models.Tournament;
import com.example.tabletennistournament.repositories.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;

    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    public Tournament getById(Long id) {
        return tournamentRepository.findById(id).orElseThrow(
                () -> new AppException("Tournament not found with id: " + id, HttpStatus.NOT_FOUND)
        );
    }

    public Long save(Tournament tournament) {
        tournament.setDateTime(LocalDateTime.now());
        tournament.setTournamentType(TournamentType.ACTIVE);
        return tournamentRepository.save(tournament).getId();
    }

    public Long deleteById(Long id) {
        tournamentRepository.deleteById(id);
        return id;
    }

    public Long updateById(Long id, Tournament updatedTournament) {
        Tournament tournament = getById(id);
        tournament.setUsers(updatedTournament.getUsers());

        return tournamentRepository.save(tournament).getId();
    }

}
