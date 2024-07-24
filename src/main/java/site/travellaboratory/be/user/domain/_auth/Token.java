package site.travellaboratory.be.user.domain._auth;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Token {
    String token;
    LocalDateTime expiredAt;

    @Builder
    public Token(String token, LocalDateTime expiredAt) {
        this.token = token;
        this.expiredAt = expiredAt;
    }
}
