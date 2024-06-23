package site.travellaboratory.be.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.travellaboratory.be.config.interceptor.AuthorizationInterceptor;
import site.travellaboratory.be.resolver.AuthenticatedUserIdResolver;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;
    private final AuthenticatedUserIdResolver authenticatedUserIdResolver;

    private final List<String> PASS_URL = List.of(
        "/api/*/auth/login",
        "/api/*/auth/join",
        "/api/*/auth/nickname",
        "/api/*/auth/username",
        "/api/*/auth/reissue-token",
        "/api/*/auth/pw-inquiry/email",
        "/api/v1/auth/pw-inquiry/verification",
        "/api/v1/oauth/login",
        "/api/v1/banner/articles",
        "/api/v1/auth/pw-inquiry/renewal"
    );

    private final List<String> DEFAULT_EXCLUDE = List.of(
        "/",
        "favicon.ico",
        "/error"
    );

    private final List<String> SWAGGER = List.of(
        "/swagger-ui/index.html",
        "/swagger-ui/**",
        "/v3/api-docs/**"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인증 인터셉터 등록
        registry.addInterceptor(authorizationInterceptor)
            .excludePathPatterns(PASS_URL)
            .excludePathPatterns(DEFAULT_EXCLUDE)
            .excludePathPatterns(SWAGGER);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserIdResolver);
    }
}
