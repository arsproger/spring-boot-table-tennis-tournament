package com.example.tabletennistournament.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Test {
    private LocalDateTime dateTime;
    private String name;
    private String users;
}