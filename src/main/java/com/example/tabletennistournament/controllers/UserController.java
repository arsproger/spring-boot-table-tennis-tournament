package com.example.tabletennistournament.controllers;

import com.example.tabletennistournament.dtos.UserDto;
import com.example.tabletennistournament.mappers.UserMapper;
import com.example.tabletennistournament.security.DetailsUser;
import com.example.tabletennistournament.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal DetailsUser user, Model model) {
        model.addAttribute("user", userMapper.map(userService.getById(user.getUser().getId())));
        return "profile";
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return new ResponseEntity<>(userMapper.map(userService.getAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(userMapper.map(userService.getById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody UserDto user) {
        return new ResponseEntity<>(userService.save(userMapper.map(user)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteById(id), HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Long> updateById(@PathVariable Long id, @RequestBody UserDto user) {
        return new ResponseEntity<>(userService.updateById(id, userMapper.map(user)), HttpStatus.OK);
    }

}
