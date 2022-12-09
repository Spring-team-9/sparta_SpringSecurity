package com.example.team9_SpringSecurity.service;

import com.example.team9_SpringSecurity.dto.LoginRequestDto;
import com.example.team9_SpringSecurity.dto.MessageDto;
import com.example.team9_SpringSecurity.dto.SignupRequestDto;
import com.example.team9_SpringSecurity.dto.StatusEnum;
import com.example.team9_SpringSecurity.entity.User;
import com.example.team9_SpringSecurity.entity.UserRoleEnum;
import com.example.team9_SpringSecurity.repository.UserRepository;
import com.example.team9_SpringSecurity.util.error.CustomException;
import com.example.team9_SpringSecurity.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import static com.example.team9_SpringSecurity.util.error.ErrorCode.EXIST_USER;
import static com.example.team9_SpringSecurity.util.error.ErrorCode.LOGIN_MATCH_FAIL;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final String ADMIN_TOKEN = "admin";

    public MessageDto signup(SignupRequestDto dto){
        String username = dto.getUsername();
        String password = dto.getPassword();
        UserRoleEnum role = ADMIN_TOKEN.equals(dto.getAdminToken()) ? UserRoleEnum.ADMIN : UserRoleEnum.USER ;

        if(userRepository.findByUsername(username).isPresent()){
            throw new CustomException(EXIST_USER);
        };

        User user = new User(username, password, role);
        userRepository.save(user);

        return new MessageDto(StatusEnum.OK);
    }

    public MessageDto login(LoginRequestDto dto, HttpServletResponse response){
        String username = dto.getUsername();
        String password = dto.getPassword();

        User user = userRepository.findByUsernameAndPassword(username, password).orElseThrow(
                ()-> new CustomException(LOGIN_MATCH_FAIL)
        );

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(),  user.getRole()));  // 메소드사용하려면 의존성주입 먼저

        return new MessageDto(StatusEnum.OK);
    }
}