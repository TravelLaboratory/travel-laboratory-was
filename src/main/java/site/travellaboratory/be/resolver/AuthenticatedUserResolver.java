package site.travellaboratory.be.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import site.travellaboratory.be.common.annotation.AuthenticatedUser;
import site.travellaboratory.be.user.repository.entity.UserEntity;
import site.travellaboratory.be.user.service.UserAuthService;
import site.travellaboratory.be.user.service.domain.User;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserResolver implements HandlerMethodArgumentResolver {

    private final UserAuthService userAuthService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 지원하는 파라미터 체크, 어노테이션 체크

        // (1) 어노테이션 있는지 체크
        boolean annotation = parameter.hasParameterAnnotation(AuthenticatedUser.class);
        boolean parameterType = parameter.getParameterType().equals(User.class);

        return (annotation && parameterType);
    }

    // supportsParameter 에서 return true 일 때 해당 메서드 실행
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // interceptor 에서 RequestContextHolder set 했던 userId 꺼내기
        RequestAttributes requestContext = RequestContextHolder.getRequestAttributes();
        Object userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        // 꺼내 userId를 authService - authRepository 에서 찾아온다.
        UserEntity userEntity = userAuthService.getAuthUserWithThrow(
            Long.parseLong(userId.toString()));

        // todo : password 및 전달할 필요없는 것들 제거하고 클래스 새로 생성할 것!! 현재는 User로 임시 작성
        // 사용자 정보 설정
        return User.builder()
            .id(userEntity.getId())
            .userName(userEntity.getUserName())
            .password(userEntity.getPassword())
            .role(userEntity.getRole())
            .nickName(userEntity.getNickName())
            .profileImgUrl(userEntity.getProfileImgUrl())
            .registerAt(userEntity.getRegisterAt())
            .updateAt(userEntity.getUpdateAt())
            .deleteAt(userEntity.getDeleteAt())
            .refreshToken(userEntity.getRefreshToken())
            .build();
    }
}
