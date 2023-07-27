package com.example.tabletennistournament.controllers;

import com.example.tabletennistournament.dtos.UserDto;
import com.example.tabletennistournament.mappers.UserMapper;
import com.example.tabletennistournament.services.UserService;
import com.example.tabletennistournament.util.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registrationPage(@ModelAttribute("user") UserDto user) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid UserDto userDTO, BindingResult bindingResult) {
        userValidator.validate(userMapper.map(userDTO), bindingResult);

        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.save(userMapper.map(userDTO));
        return "redirect:/auth/login";
    }

}
