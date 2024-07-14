package site.travellaboratory.be.domain.user.user;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.travellaboratory.be.domain.user.enums.UserStatus;

@Builder
@Getter
@RequiredArgsConstructor
public class User {

    private final Long id;
    private final String nickname;
    private final String profileImgUrl;
    private final String introduce;
    private final UserStatus status;

    public static User create(String nickname) {
        return User.builder()
            .nickname(nickname)
            .status(UserStatus.ACTIVE)
            .build();
    }
}
