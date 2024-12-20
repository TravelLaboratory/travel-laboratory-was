package site.travellaboratory.be.user.infrastructure.jwt.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;

@Slf4j
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtParser parser;

    public AuthorizationInterceptor(@Value("${jwt.secret-key}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler) throws Exception {
        log.info("Authorization Interceptor url : {}", request.getRequestURI());

        // WEB, Chrome 의 경우 GET, POST 전에 OPTIONS을 통해 해당 메서드를 지원하는지 체크하는데 = PASS
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // js, html, png, resource 요청 -PASS
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        /*
         * Header 검증 로직
         * */
        // (1) 헤더에서 authorization-token 꺼내고
        String accessToken = request.getHeader("authorization-token");
        // 토큰이 없다면?
        if (accessToken == null) {
            throw new BeApplicationException(ErrorCodes.TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND,
                HttpStatus.BAD_REQUEST);
        }

        // (2) 토큰이 유효한지 체크 - 없다면 BAD_REQUEST 후, userId 반환
        Long userId = getUserIdBy(accessToken);

        // (4)-1 현재 요청 request Context 에다가 userId를 저장한다.
        // (4)-2 범위는 이번 요청동안만! SCOPE_REQUEST
        RequestAttributes requestContext = Objects.requireNonNull(
            RequestContextHolder.getRequestAttributes());
        requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
        return true;
    }

    private Long getUserIdBy(String token) {
        Claims claims = parseAccessTokenClaims(token);
        return Long.parseLong(claims.get("userId").toString());
    }

    private Claims parseAccessTokenClaims(String token) {
        try {
            return parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_EXPIRED_TOKEN, HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException | io.jsonwebtoken.security.SecurityException e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_INVALID_TOKEN, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new BeApplicationException(ErrorCodes.TOKEN_AUTHORIZATION_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
