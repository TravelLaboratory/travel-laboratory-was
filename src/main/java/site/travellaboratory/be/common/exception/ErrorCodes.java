package site.travellaboratory.be.common.exception;

import org.jetbrains.annotations.NotNull;

// todo enum을 각각의 controller에 맞게끔 나눠주기
public enum ErrorCodes {

    // 회원가입
    AUTH_DUPLICATED_USER_NAME("이미 존재하는 아이디입니다.", 1000L),
    AUTH_DUPLICATED_NICK_NAME("중복된 닉네임입니다.", 1001L),
    // 로그인
    AUTH_USER_NOT_FOUND("사용자를 찾을 수 없습니다.", 1002L),
    AUTH_INVALID_PASSWORD("잘못된 비밀번호입니다.", 1003L),

    // todo : tokenErrorCode 로 분리
    TOKEN_INVALID_TOKEN("유효하지 않은 토큰", 2000L),
    TOKEN_EXPIRED_TOKEN("만료된 토큰", 2001L),
    TOKEN_TOKEN_EXCEPTION("토큰 알수없는 에러", 2002L),
    TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND("인증 헤더 토큰이 없음", 2003L),
    TOKEN_AUTHORIZATION_FAIL("인증 실패", 2004L),

    TOKEN_INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰", 2005L),

    // NPE
    NULL_POINT_EXCEPTION("NPE", 9998L),
    // Runtime Exception
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", 9999L)
    ;
    public final @NotNull String message;
    public final @NotNull Long code;

    ErrorCodes(@NotNull String message, @NotNull Long code) {
        this.message = message;
        this.code = code;
    }
}
