package com.example.team9_SpringSecurity.controller;

import com.example.team9_SpringSecurity.dto.LoginRequestDto;
import com.example.team9_SpringSecurity.dto.MessageDto;
import com.example.team9_SpringSecurity.dto.SignupRequestDto;
import com.example.team9_SpringSecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<MessageDto> signup(@RequestBody @Valid SignupRequestDto dto){
        MessageDto messageDto = userService.signup(dto);
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<MessageDto> login(@RequestBody LoginRequestDto dto, HttpServletResponse response){
        MessageDto messageDto = userService.login(dto, response);
        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }
}
