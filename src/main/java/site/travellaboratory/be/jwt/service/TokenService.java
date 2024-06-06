package site.travellaboratory.be.jwt.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.jwt.JwtTokenManager;
import site.travellaboratory.be.jwt.model.Token;
import site.travellaboratory.be.user.repository.UserAuthRepository;
import site.travellaboratory.be.user.repository.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenManager jwtTokenManager;
    private final UserAuthRepository userAuthRepository;

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

    public Long validationAccessToken(@NotNull String token) {
        Map<String, Object> map = jwtTokenManager.validationTokenWithThrow(token);
        Object userId = map.get("userId");

        Objects.requireNonNull(userId, () -> {
            throw new BeApplicationException(ErrorCodes.NULL_POINT_EXCEPTION,
                HttpStatus.INTERNAL_SERVER_ERROR);
        });

        return Long.parseLong(userId.toString());
    }

    @Transactional
    public Token refreshAccessToken(@NotNull String accessToken, @NotNull String refreshToken) {
        // 액세스 토큰 검증
        try {
            jwtTokenManager.validationTokenWithThrow(accessToken);
        } catch (BeApplicationException e) {
            if (e.getErrorCodes() != ErrorCodes.TOKEN_EXPIRED_TOKEN) {
                // 만료된 토큰 예외가 아닌 다른 예외 발생 시
//                throw new BeApplicationException(ErrorCodes.TOKEN_INCORRECT_TOKEN_REQUEST, HttpStatus.BAD_REQUEST);
                System.out.println("e = " + e);
                throw e;
            }
        }

        // 리프레시 토큰 검증
        Map<String, Object> refreshTokenClaims = jwtTokenManager.validationTokenWithThrow(refreshToken);
        Long userId = Long.parseLong(refreshTokenClaims.get("userId").toString());

        // DB에 저장된 리프레시 토큰과 비교
        UserEntity userEntity = userAuthRepository.findById(userId)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.AUTH_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!refreshToken.equals(userEntity.getRefreshToken())) {
            throw new BeApplicationException(ErrorCodes.TOKEN_INVALID_REFRESH_TOKEN, HttpStatus.UNAUTHORIZED);
        }

        // 새로운 액세스 토큰 발급
        return issueAccessToken(userId);
    }
}
