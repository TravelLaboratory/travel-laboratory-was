package site.travellaboratory.be.user.domain._auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AccessToken {
    String accessToken;
    String expiredAt;

    @Builder
    public AccessToken(String accessToken, String expiredAt) {
        this.accessToken = accessToken;
        this.expiredAt = expiredAt;
    }
}