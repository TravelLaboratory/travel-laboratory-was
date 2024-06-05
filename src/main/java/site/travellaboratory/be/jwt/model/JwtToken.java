package site.travellaboratory.be.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Getter
public class JwtToken {
    private @NotNull Token accessToken;
    private @NotNull Token refreshToken;
}


