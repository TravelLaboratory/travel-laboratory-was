package site.travellaboratory.be.user.domain;

import lombok.Builder;
import lombok.Getter;
import site.travellaboratory.be.user.domain.enums.UserStatus;


@Getter
public class User {

    private final Long id;
    private final String nickname;
    private final String profileImgUrl;
    private final String introduce;
    private final UserStatus status;

    @Builder
    public User(Long id, String nickname, String profileImgUrl, String introduce,
        UserStatus status) {
        this.id = id;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.introduce = introduce;
        this.status = status;
    }

    public static User register(String nickname) {
        return User.builder()
            .nickname(nickname)
            .status(UserStatus.ACTIVE)
            .build();
    }
}
