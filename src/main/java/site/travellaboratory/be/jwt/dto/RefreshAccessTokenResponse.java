package site.travellaboratory.be.jwt.dto;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.jwt.model.Token;

public record RefreshAccessTokenResponse(
    @NotNull String accessToken
) {

    public static RefreshAccessTokenResponse fromDomain(Token token
    ) {
        Objects.requireNonNull(token, () -> {
            throw new BeApplicationException(ErrorCodes.NULL_POINT_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        });

        return new RefreshAccessTokenResponse(
            token.getToken()
        );
    }
}