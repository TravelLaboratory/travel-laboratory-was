package site.travellaboratory.be.common.presentation.resolver;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserIdResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 지원하는 파라미터 체크, 어노테이션 체크
        boolean annotation = parameter.hasParameterAnnotation(UserId.class);
        boolean parameterType = parameter.getParameterType().equals(Long.class);

        return (annotation && parameterType);
    }

    @Override
    public Object resolveArgument(
        @NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
        @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // supportsParameter 에서 return true 일 때 해당 메서드 실행
        RequestAttributes requestContext = RequestContextHolder.getRequestAttributes();

        if (requestContext == null) {
            throw new BeApplicationException(ErrorCodes.BAD_REQUEST_REQUEST_ATTRIBUTES_MISSING, HttpStatus.BAD_REQUEST);
        }

        Object userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        if (userId == null) {
            throw new BeApplicationException(ErrorCodes.BAD_REQUEST_USER_ID_MISSING, HttpStatus.BAD_REQUEST);
        }

        return Long.parseLong(userId.toString());
    }
}
