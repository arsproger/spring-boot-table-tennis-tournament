package com.example.tabletennistournament.services;

import com.example.tabletennistournament.exceptions.AppException;
import com.example.tabletennistournament.models.User;
import com.example.tabletennistournament.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new AppException("User not found with id: " + id, HttpStatus.NOT_FOUND)
        );
    }

    public Long save(User user) {
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
