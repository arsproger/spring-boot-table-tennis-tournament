package com.example.tabletennistournament.mappers;

import com.example.tabletennistournament.dtos.UserDto;
import com.example.tabletennistournament.models.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    User map(UserDto dto);

    UserDto map(User entity);

    List<UserDto> map(List<User> entities);
}
