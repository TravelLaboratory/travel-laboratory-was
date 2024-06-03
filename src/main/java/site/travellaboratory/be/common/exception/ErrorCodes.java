package site.travellaboratory.be.common.exception;

import org.jetbrains.annotations.NotNull;

// todo enum을 각각의 controller에 맞게끔 나눠주기
public enum ErrorCodes {

    // 회원가입
    AUTH_DUPLICATED_USER_NAME("이미 존재하는 아이디입니다.", 1001L),
    AUTH_DUPLICATED_NICK_NAME("중복된 닉네임입니다.", 1001L),
    USER_NOT_FOUND("회원가입하지 않은 사용자입니다.", 1000L),
    USER_INCORRECT_PASSWORD("비밀번호를 확인해주세요.", 1010L),


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
