package com.icia.wapoo.jwt.service;

import com.icia.wapoo.model.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import java.util.Base64;
import java.util.Date;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Service("jwtService")
public class JwtService {

//    @Value("${jwt.salt}")
//    private static String SALT;
    private String secretKey = "testxxxtesttesttesttesttest";

//    @Value("${jwt.expmin}")
//    private Long expMin;
    private static final long expMin = 60;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }




    /**
     * 토큰 생성 - 노철희
     *
     * @param member 멤버객체로 토큰을 생선한다. Id, nickname, role이 담긴다.
     * @return
     */
    public String create(final Member member){
        log.trace("time: {}", expMin);

        System.out.println("JwtService.create : 토큰 생성을 시작합니다.");
        Date NOW = new Date();
        Claims claims = Jwts.claims()
                .setSubject("access_token")
                .setIssuedAt(NOW)
                .setExpiration(new Date(NOW.getTime() + (1000*60*60)));
        //값을 세팅한다.
        claims.put("memberId", member.getMemberId());
        claims.put("nickname", member.getNickname());
        claims.put("role", member.getRole());

        final String jwt = Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .setClaims(claims)
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact();

        System.out.println("JwtService.create : 토큰 생성이 완료되었습니다.");
        log.debug("토큰 발행 : {}", jwt);
        return jwt;
    }


    // Request의 Header 에서 값 추출.
    public String resolveToken(HttpServletRequest request) {
    
        return request.getHeader("Authorization");
    }

    // 토큰에서 정보 해독
    public Map<String, Object> getUserInfo(String token) {
      
        Map<String, Object> claimMap = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey) // Set Key
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();

            claimMap = claims;
            System.out.println(claimMap);
        } catch (ExpiredJwtException e) { // 토큰이 만료되었을 경우
            System.out.println(e);

        } catch (Exception e) { // 그외 에러났을 경우
            System.out.println(e);

        }
        return claimMap;
    }


    // 토큰 유효 + 만료일자
    public boolean validateToken(String jwtToken) {
        try {
           
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
