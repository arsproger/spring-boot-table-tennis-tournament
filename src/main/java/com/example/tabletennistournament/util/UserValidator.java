package com.example.tabletennistournament.util;

import com.example.tabletennistournament.models.User;
import com.example.tabletennistournament.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.getByUsername(user.getUsername()).isPresent())
            errors.rejectValue("username", "", "Логин уже зарегистрирован!");
    }

}
