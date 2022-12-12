package com.example.team9_SpringSecurity.jwt;

import com.example.team9_SpringSecurity.entity.UserRoleEnum;
import com.example.team9_SpringSecurity.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j // Simple Logging Facade for Java의 약자로 로깅에 대한 추상 레이어를 제공하는 인터페이스 모음
@Component  // Bean으로 등록해 다른데서도 의존성 주입 가능하게
@RequiredArgsConstructor    // final 등의 필수요소에 대한 생성자 생성
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization"; // 헤더 이름
    public static final String AUTHORIZATION_KEY = "auth";  // 헤더 키 값
    private static final String BEARER_PREFIX = "Bearer ";  // 인증 타입. 타입은 이외에도 Basic, Digest, HOBA, Mutual, AWS4-HMAC-SHA256 등이 있는데 JWT와 OAuth에 적합한건 Bearer타입
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 이게 1시간

    private final UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.secret.key}") // @Value 필드나 메서드, 생성자의 파라미터 수준에서 값을 주입해주는 어노테이션. 해당값은 application.properties에 있음
    private String secretKey;   // 위 어노테이션에서 주입한 값이 바로 아래 변수에 대입된다.

    private Key key;            // 생성된 키를 저장할 변수
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 암호화 알고리즘 HS256 선택

    @PostConstruct  // 의존성 주입이 이루어진 후 초기화를 수행하는 메소드
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);   // Base64로 인코딩 되어있는 문자열을 byte 배열로 변환
        //* Base64 (64진법) : 바이너리(2진) 데이터를 문자 코드에 영향을 받지 않는 공통 ASCII문자로 표현하기 위해 만들어진 인코딩
        key = Keys.hmacShaKeyFor(bytes); // 바이트 배열을 HMAC-SHA 알고리즘을 사용해 Key객체로 반환, 이를 key변수에 대입
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role){
        Date date = new Date();

        // 토큰 Builder반환
        return BEARER_PREFIX +      // BEARER : 인증 타입중 하나로 JWT 또는 OAuth에 대한 토큰을 사용 (RFC 6750 문서 확인)
                Jwts.builder()
                        .setSubject(username)   // 토큰 용도
                        .claim(AUTHORIZATION_KEY, role) // payload에 들어갈 정보 조각들
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))   // 만료시간 설정
                        .setIssuedAt(date)  // 토큰 발행일
                        .signWith(key, signatureAlgorithm)  // key변수 값과 해당 알고리즘으로 sign
                        .compact(); // 토큰 생성
    }


    // Header에서 토큰 가져오기
    public String resolveToken(HttpServletRequest request){    // Http프로토콜의 request정보를 서블릿에게 전달하기 위한 목적으로 사용하는 매개변수
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // 우리가 위에서 설정한 "Authorization"의 헤더를 가져옴

        // StringUtils.hasText는 값이 있을경우 true , 공백이거나 Null이 들어온 경우 false반환
        // bearerToken.startsWith는 필드값이 해당 변수와 동일한지 확인
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7); // bearer 제외하고 나머지 문자열 반환
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token){
        try {

            //parser : parsing을 하는 도구. parsing : token에 내재된 자료 구조를 빌드하고 문법을 검사한다.
            // JwtParseBuilder인스턴스를 생성
            // 서명 검증을 위한 키를 지정 setSigningKey()
            // 스레드에 안전한 JwtPaser를 리턴하기 위해 JwtPaserBuilder의 build()메서드를 호출
            // 서버의 시크릿키로 서명한 것을 토큰화한 jws인것인지 검증???? <-- 확인 더 필요
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) { // 전: 권한 없다면 발생 , 후: JWT가 올바르게 구성되지 않았다면 발생
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {   // JWT만료
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        // 위의 내용에서 body부분 값을 가져오는 부분
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
