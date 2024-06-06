package site.travellaboratory.be.jwt.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.jwt.JwtTokenManager;
import site.travellaboratory.be.jwt.model.Token;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenManager jwtTokenManager;

    /*
    * token 에 관한 도메인 로직만 담당
    * 일단, userId를 기준으로 구현
    * */
    public Token issueAccessToken(Long userId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        return jwtTokenManager.issueAccessToken(data);
    }


    public Token issueRefreshToken(Long userId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        return jwtTokenManager.issueRefreshToken(data);
    }

    public Long validationToken(@NotNull String token) {
        Map<String, Object> map = jwtTokenManager.validationTokenWithThrow(token);
        Object userId = map.get("userId");

        Objects.requireNonNull(userId, () -> {
            throw new BeApplicationException(ErrorCodes.NULL_POINT_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        });

        return Long.parseLong(userId.toString());
    }

}
