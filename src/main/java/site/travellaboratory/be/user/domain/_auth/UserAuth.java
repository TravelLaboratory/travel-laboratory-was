package site.travellaboratory.be.user.domain._auth;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain._auth.request.UserJoinRequest;

@Builder
@Getter
@RequiredArgsConstructor
public class UserAuth {

    private final Long id;
    private final String username;
    private final String password;
    private final UserRole role;
    private final Boolean isAgreement;

    public static UserAuth create(String encodedPassword, UserJoinRequest joinRequest) {
        // 개인정보 미동의 시 에러 반환
        if (!joinRequest.isAgreement()) {
            throw new BeApplicationException(ErrorCodes.AUTH_USER_NOT_IS_AGREEMENT,
                HttpStatus.BAD_REQUEST);
        }

        return UserAuth.builder()
            .username(joinRequest.username())
            .password(encodedPassword)
            .isAgreement(true)
            .build();
    }

    public UserAuth withPassword(String password) {
        return UserAuth.builder()
            .id(this.id)
            .username(this.username)
            .password(password)
            .role(this.role)
            .isAgreement(this.isAgreement)
            .build();
    }
}
