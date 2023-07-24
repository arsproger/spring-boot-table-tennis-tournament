package com.example.tabletennistournament.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handler(RuntimeException e) {
//        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//    }

}
