package com.example.tabletennistournament.controllers;

import com.example.tabletennistournament.dtos.UserDto;
import com.example.tabletennistournament.mappers.UserMapper;
import com.example.tabletennistournament.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/main")
    public String mainPage() {
        return "loginRegister";
    }

//    @GetMapping("/login")
//    public String loginPage(Model model) {
//        model.addAttribute("isPresentEmail", false);
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String registrationPage(@ModelAttribute("user") UserDto userDTO) {
//        return "register";
//    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDto userDTO, Model model) {
        userService.save(userMapper.map(userDTO));
        return "redirect:/auth/login";
    }

}
