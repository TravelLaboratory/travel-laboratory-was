package site.travellaboratory.be.jwt.dto;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.jwt.model.JwtToken;
import site.travellaboratory.be.jwt.model.Token;

public record JwtTokenResponse(
    @NotNull String accessToken,
    @NotNull String refreshToken
) {

    public static JwtTokenResponse fromDomain(JwtToken jwtToken
        ) {
        Token accessToken = jwtToken.getAccessToken();
        Token refreshToken = jwtToken.getRefreshToken();

        Objects.requireNonNull(accessToken, () -> {
            throw new BeApplicationException(ErrorCodes.NULL_POINT_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        });
        Objects.requireNonNull(refreshToken, () -> {
            throw new BeApplicationException(
                ErrorCodes.NULL_POINT_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        });

        return new JwtTokenResponse(
            accessToken.getToken(),
            refreshToken.getToken()
        );
    }
}