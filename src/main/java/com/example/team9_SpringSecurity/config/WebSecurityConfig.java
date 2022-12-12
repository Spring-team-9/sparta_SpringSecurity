package com.example.team9_SpringSecurity.config;

import com.example.team9_SpringSecurity.jwt.JwtSecurityFilter;
import com.example.team9_SpringSecurity.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity                                                             // 스프링 Security 지원을 가능하게 하는 어노테이션
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();                                                                                  //일반적인 루트가 아닌 다른 방식으로 요청시 거절, header에 id, pw가 아닌 token(jwt)을 달고 간다. 그래서 basic이 아닌 bearer를 사용한다.
        http.httpBasic().disable()
                .authorizeRequests()                                                                            //요청에 대한 사용권한에 대해서 체크함
                .antMatchers("/api/user/**").permitAll()                                            // 해당 Url의 하위 url들은 권한을 전체 허용한다.
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .anyRequest().authenticated()                                                                   // 그 외 요청은 authentication이라는 객체가 Security Context에 있는지 확인함
                .and()
                .addFilterBefore(new JwtSecurityFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);                                            // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣고, 토큰에 저장된 유저정보를 활용하여야 하기 때문에 CustomUserDetailService 클래스를 생성한다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}