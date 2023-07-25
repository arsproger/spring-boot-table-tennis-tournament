package com.example.tabletennistournament.services;

import com.example.tabletennistournament.enums.TournamentType;
import com.example.tabletennistournament.exceptions.AppException;
import com.example.tabletennistournament.models.Tournament;
import com.example.tabletennistournament.repositories.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final UserService userService;

    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    public Tournament getById(Long id) {
        return tournamentRepository.findById(id).orElseThrow(
                () -> new AppException("Tournament not found with id: " + id, HttpStatus.NOT_FOUND)
        );
    }

    public Long save(Tournament tournament, Long userId) {
        if (tournament.getUsers().size() < 3 || tournament.getUsers().size() > 32) {
            throw new AppException("Число игроков должно быть от 3 до 32", HttpStatus.BAD_REQUEST);
        }
        String pattern = "dd-MM-yyyy HH:mm:ss";
        tournament.setDateTime(LocalDateTime.parse(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(pattern)),
                DateTimeFormatter.ofPattern(pattern)));
        tournament.setTournamentType(TournamentType.ACTIVE);
        tournament.setUserCount(tournament.getUsers().size());
        tournament.setCreator(userService.getById(userId));
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
