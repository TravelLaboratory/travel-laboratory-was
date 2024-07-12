package site.travellaboratory.be.domain.user.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.domain.user.auth.enums.UserRole;

@Builder
@Getter
@RequiredArgsConstructor
public class UserAuth {

    private final Long id;
    private final String username;
    private final String password;
    private final UserRole role;
    private final Boolean isAgreement;

    public static UserAuth create(String username, String password, Boolean isAgreement) {
        // 개인정보 미동의 시 에러 반환
        if (isAgreement) {
            throw new BeApplicationException(ErrorCodes.AUTH_USER_NOT_IS_AGREEMENT,
                HttpStatus.BAD_REQUEST);
        }

        return UserAuth.builder()
            .username(username)
            .password(password)
            .isAgreement(isAgreement)
            .build();
    }
}
