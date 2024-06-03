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
