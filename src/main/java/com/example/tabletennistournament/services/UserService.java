package com.example.tabletennistournament.services;

import com.example.tabletennistournament.enums.UserRole;
import com.example.tabletennistournament.exceptions.AppException;
import com.example.tabletennistournament.models.User;
import com.example.tabletennistournament.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new AppException("User not found with id: " + id, HttpStatus.NOT_FOUND)
        );
    }

    public Long save(User user) {
        user.setRole(UserRole.ROLE_USER);
        String pattern = "dd-MM-yyyy HH:mm:ss";
        user.setRegisterDate(LocalDateTime.parse(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(pattern)),
                DateTimeFormatter.ofPattern(pattern)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNumberOfMatchesWon(0);
        user.setNumberOfTournamentsWon(0);
        user.setNumberOfMatchesPlayed(0);
        user.setNumberOfTournamentsPlayed(0);
        user.setMatchWinningPercentage(0.0);
        user.setTournamentWinningPercentage(0.0);
        user.setTournaments(new ArrayList<>());
        return userRepository.save(user).getId();
    }

    public Long deleteById(Long id) {
        userRepository.deleteById(id);
        return id;
    }

    public Long updateById(Long id, User updatedUser) {
        User user = getById(id);
        user.setFullName(updatedUser.getFullName());
        user.setUsername(updatedUser.getUsername());
        return userRepository.save(user).getId();
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getPlayersRankedByWinPercentage() {
        List<User> users = getAll();
        for (User user : users) {
            Double matchWinningPercentage = user.getNumberOfMatchesPlayed() != 0
                    ? user.getNumberOfMatchesWon().doubleValue()
                    / user.getNumberOfMatchesPlayed().doubleValue() * 100.0
                    : 0.0;
            user.setMatchWinningPercentage(Double.parseDouble(
                    new DecimalFormat("#.##")
                            .format(matchWinningPercentage).replace(',', '.')));
            Double tournamentWinningPercentage = user.getNumberOfTournamentsPlayed() != 0
                    ? user.getNumberOfTournamentsWon().doubleValue()
                    / user.getNumberOfTournamentsPlayed() * 100.0
                    : 0.0;
            user.setTournamentWinningPercentage(Double.parseDouble(
                    new DecimalFormat("#.##")
                            .format(tournamentWinningPercentage).replace(',', '.')));
            userRepository.save(user);
        }

        List<User> sortedUsers = new ArrayList<>(users.stream()
                .sorted(Comparator.comparingDouble(
                        user -> user.getMatchWinningPercentage() + user.getTournamentWinningPercentage()))
                .toList());
        Collections.reverse(sortedUsers);
        return sortedUsers;
    }

}
