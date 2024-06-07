package site.travellaboratory.be.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.jwt.service.TokenService;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

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
            throw new BeApplicationException(ErrorCodes.TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        // 토큰이 있다면?
        // (2) accessToken을 validation 거치고
        Long userId = tokenService.validationAccessToken(accessToken);


        // (3) userId가 있다면?
        if (userId != null) {
            // (4)-1 현재 요청 request Context에다가 userId를 저장한다.
            // (4)-2 범위는 이번 요청동안만! SCOPE_REQUEST
            RequestAttributes requestContext = Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
            return true;
        }

        // userId가 없다면?
        throw new BeApplicationException(ErrorCodes.TOKEN_AUTHORIZATION_FAIL, HttpStatus.BAD_REQUEST);
    }
}
