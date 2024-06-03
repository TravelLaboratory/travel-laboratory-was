package site.travellaboratory.be.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class JwtTokenUtils {

    // 토큰 생성
    public static String generateToken(
        @NotNull String userName,
        @NotNull String key, //userName을 넣은 값을 암호화할 때 사용하는 암호화 키
        @NotNull Long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
            .signWith(getKey(key), SignatureAlgorithm.HS256)
            .compact();
    }

    // 키 생성
    private static Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}