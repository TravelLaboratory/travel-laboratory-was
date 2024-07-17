package site.travellaboratory.be.common.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.HandlerMethod;
import site.travellaboratory.be.common.annotation.UserId;

@Configuration
public class SwaggerConfig {

    @Value("${servers.url}")
    private String serverUrl;

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    @Profile("prod")
    public OpenApiCustomizer prodOpenApiCustomizer() {
        return openApi -> openApi.servers(
            List.of(new Server().url(serverUrl).description("production server")));
    }

    @Bean
    public OperationCustomizer customizeOperation() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (handlerMethod.getMethod().getParameters() != null) {
                for (java.lang.reflect.Parameter parameter : handlerMethod.getMethod().getParameters()) {
                    if (parameter.isAnnotationPresent(UserId.class)) {
                        operation.getParameters().removeIf(p -> p.getName().equals(parameter.getName()));
                    }
                }
            }
            return operation;
        };
    }
}
