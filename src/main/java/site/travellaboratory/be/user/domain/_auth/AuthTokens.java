package site.travellaboratory.be.user.domain._auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthTokens {
    String accessToken;
    String refreshToken;
    String expiredAt;

    @Builder
    public AuthTokens(String accessToken, String refreshToken, String expiredAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredAt = expiredAt;
    }
}