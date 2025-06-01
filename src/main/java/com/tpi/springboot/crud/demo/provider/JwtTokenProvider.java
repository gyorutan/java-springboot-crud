package com.tpi.springboot.crud.demo.provider;

import com.tpi.springboot.crud.demo.exception.CustomException;
import com.tpi.springboot.crud.demo.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Getter
@Setter
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-milliseconds}")
    private long accessTokenExpirationMilliseconds;

    @Value("${jwt.refresh-token-expiration-milliseconds}")
    private long refreshTokenExpirationMilliseconds;

    private Key signingKey;

    @PostConstruct
    protected void init() {
        // Base64 디코딩 후 키 생성
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes());
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenExpirationMilliseconds);

        return Jwts.builder()
                .subject(String.valueOf(username))
                .issuedAt(now)
                .expiration(validity)
                .signWith(signingKey)
                .compact();

    }

    public String createRefreshToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenExpirationMilliseconds);

        return Jwts.builder()
                .subject(String.valueOf(username))
                .issuedAt(now)
                .expiration(validity)
                .signWith(signingKey)
                .compact();

    }

//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    public String getUsername(String token) {
       return Jwts.parser()
               .verifyWith((SecretKey) signingKey)
               .build()
               .parseSignedClaims(token)
               .getPayload()
               .getSubject();
    }

    // --- JWT 토큰 유효성 검증 함수 ---
    public void validateToken(String token) {
        try {
            // 토큰을 파싱하고 서명을 검증합니다.
            // 만료되었거나 서명이 유효하지 않으면 예외가 발생합니다.
            Jwts.parser()
                    .verifyWith((SecretKey) signingKey) // 토큰 서명 검증에 사용할 Secret Key
                    .build()
                    .parseSignedClaims(token); // 토큰 파싱 및 JWS(JSON Web Signature) 검증
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }
    }

//    public boolean validateToken(String token) {
//        try {
//            Jws<Claims> claims = Jwts.parser()
//                    .verifyWith((SecretKey) signingKey)
//                    .build()
//                    .parseSignedClaims(token);
//            return !claims.getPayload().getExpiration().before(new Date());
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new JwtAuthenticationException("만료되었거나 잘못된 토큰입니다");
//        }
//    }
}
