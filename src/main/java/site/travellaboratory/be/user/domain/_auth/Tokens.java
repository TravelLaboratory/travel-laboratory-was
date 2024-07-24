package site.travellaboratory.be.user.domain._auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Tokens {

    Token accessToken;
    Token refreshToken;

    @Builder
    public Tokens(Token accessToken, Token refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}