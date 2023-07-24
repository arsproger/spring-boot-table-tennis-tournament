package com.example.tabletennistournament.services;

import com.example.tabletennistournament.enums.UserRole;
import com.example.tabletennistournament.exceptions.AppException;
import com.example.tabletennistournament.models.User;
import com.example.tabletennistournament.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        user.setRegisterDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNumberOfMatchesWon(0);
        user.setNumberOfTournamentsWon(0);
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

}
