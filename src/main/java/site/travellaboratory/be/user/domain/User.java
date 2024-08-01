package site.travellaboratory.be.user.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain._auth.request.UserJoinRequest;
import site.travellaboratory.be.user.domain.enums.UserStatus;


@Getter
@Builder
public class User {

    private final Long id;
    private final String username;
    private final String password;
    private final UserRole role;
    private final String nickname;
    private final String profileImgUrl;
    private final String introduce;
    private final Boolean isAgreement;
    private final UserStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public User(Long id, String username, String password, UserRole role, String nickname,
        String profileImgUrl, String introduce, Boolean isAgreement, UserStatus status,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.introduce = introduce;
        this.isAgreement = isAgreement;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User register(String encodedPassword, UserJoinRequest joinRequest) {
        // 개인정보 미동의 시 에러 반환
        if (!joinRequest.isAgreement()) {
            throw new BeApplicationException(ErrorCodes.AUTH_USER_NOT_IS_AGREEMENT,
                HttpStatus.BAD_REQUEST);
        }

        return User.builder()
            .username(joinRequest.username())
            .password(encodedPassword)
            .role(UserRole.USER)
            .nickname(joinRequest.nickname())
            .isAgreement(true)
            .status(UserStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    public User withPassword(String password) {
        return User.builder()
            .id(this.id)
            .username(this.username)
            .password(password)
            .role(this.role)
            .nickname(this.nickname)
            .profileImgUrl(this.profileImgUrl)
            .introduce(this.introduce)
            .isAgreement(this.isAgreement)
            .status(this.status)
            .createdAt(this.createdAt)
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
