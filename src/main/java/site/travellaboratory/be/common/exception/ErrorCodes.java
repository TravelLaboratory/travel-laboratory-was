package site.travellaboratory.be.common.exception;

import org.jetbrains.annotations.NotNull;

// todo enum을 각각의 controller에 맞게끔 나눠주기
public enum ErrorCodes {

    // 회원가입
    AUTH_USER_NOT_IS_AGREEMENT("개인정보 수집 미동의", 1000L),
    AUTH_DUPLICATED_USER_NAME("이미 존재하는 아이디입니다.", 1001L),
    AUTH_DUPLICATED_NICK_NAME("중복된 닉네임입니다.", 1002L),
    // 로그인
    AUTH_USER_NOT_FOUND("사용자를 찾을 수 없습니다.", 1010L),
    AUTH_INVALID_PASSWORD("잘못된 비밀번호입니다.", 1011L),

    // todo : tokenErrorCode 로 분리
    TOKEN_INVALID_TOKEN("유효하지 않은 토큰", 2000L),
    TOKEN_EXPIRED_TOKEN("만료된 토큰", 2001L),
    TOKEN_TOKEN_EXCEPTION("토큰 알수없는 에러", 2002L),
    TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND("토큰이 존재하지 않음", 2003L),
    TOKEN_AUTHORIZATION_FAIL("인증 실패", 2004L),

    TOKEN_INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰", 2005L),
    //    REFRESH_TOKEN_NOT_CORRECT_USER("로그인한 유저의 리프레시 토큰이 아닙니다.", 2006L),
    TOKEN_NOT_EXPIRED_ACCESS_TOKEN("만료되지 않은 토큰", 2007L),
    REFRESH_TOKEN_EXPIRED_TOKEN("만료된 리프레시 토큰 - 재로그인", 2008L),
    REFRESH_TOKEN_TOKEN_EXCEPTION("리프레시 토큰 에러", 2009L),

    // 비밀번호 질문, 답 관련
    PASSWORD_INVALID_QUESTION("유효하지 않은 질문", 2010L),

    // 비밀번호 문의 - 답변
    PASSWORD_INVALID_EMAIL("유효하지 않은 이메일", 2011L),
    PASSWORD_INQUIRY_INVALID_ANSWER("올바르지 않은 답변", 2012L),

    // 유저 관련
    USER_NOT_FOUND("존재하지 않는 유저", 3000L),

    // 후기 (review)
    // 후기 작성
    REVIEW_POST_INVALID("[후기 작성] - 유효하지 않은 여행 계획 ID", 4000L),
    REVIEW_POST_NOT_USER("[후기 작성] - 여행 계획 작성자만 해당 여행 계획의 후기를 작성할 수 있습니다.", 4001L),
    REVIEW_POST_EXIST("[후기 작성] - 각 여행 계획에 대한 후기는 한 개만 작성할 수 있습니다.", 4002L),
    // 후기 수정
    REVIEW_UPDATE_INVALID("[후기 수정] - 유효하지 않은 후기 ID", 4003L),
    REVIEW_UPDATE_NOT_USER("[후기 수정] - 본인의 후기가 아닙니다.", 4004L),
    // 후기 삭제
    REVIEW_DELETE_INVALID("[후기 삭제] - 유효하지 않은 후기 ID", 4005L),
    REVIEW_DELETE_NOT_USER("[후기 삭제] - 본인의 후기가 아닙니다.", 4006L),
    // 후기 좋아요
    REVIEW_LIKE_INVALID("[후기 좋아요] - 유효하지 않은 후기 ID", 4007L),
    // 후기 상세 조회
    REVIEW_READ_DETAIL_INVALID("[후기 상세 조회] - 유효하지 않은 후기 ID", 4040L),
    REVIEW_READ_DETAIL_NOT_AUTHORIZATION("[후기 상세 조회] - 해당 후기에 접근 권한 없음", 4041L),
    // 후기 작성 전 조회
    REVIEW_BEFORE_POST_INVALID("[후기 작성 전 조회] - 유효하지 않은 여행 계획 ID", 4050L),
    REVIEW_BEFORE_POST_NOT_USER("[후기 작성 전 조회] - 여행 계획 작성자만 해당 여행 계획의 후기를 작성할 수 있습니다.", 4051L),
    REVIEW_BEFORE_POST_EXIST("[후기 작성 전 조회] - 각 여행 계획에 대한 후기는 한 개만 작성할 수 있습니다.", 4052L),
    REVIEW_BEFORE_POST_NOT_EXIST_SCHEDULES("[후기 작성 전 조회] - 상세 일정 작성 후 후기 작성이 가능합니다.", 4053L),
    // 프로필 - 리뷰 전체 조회 (페이지네이션)
    PROFILE_REVIEW_READ_USER_NOT_FOUND("[프로필 - 후기 전체 조회] - 유효하지 않은 User ID", 4060L),



    // 댓글
    // 댓글 작성
    COMMENT_POST_INVALID("[댓글 작성] - 유효하지 않은 후기 ID", 5000L),
    // 댓글 수정
    COMMENT_UPDATE_INVALID("[댓글 수정] - 유효하지 않은 댓글 ID", 5010L),
    COMMENT_UPDATE_NOT_USER("[댓글 수정] - 본인의 댓글이 아닙니다.", 5011L),
    // 댓글 삭제
    COMMENT_DELETE_INVALID("[댓글 삭제] - 유효하지 않은 댓글 ID", 5020L),
    COMMENT_DELETE_NOT_USER("[댓글 삭제] - 본인의 댓글가 아닙니다.", 5021L),
    // 댓글 좋아요
    COMMENT_LIKE_INVALID("[댓글 좋아요] - 유효하지 않은 댓글 ID", 5030L),
    // 댓글 전체 조회 (페이지네이션)
    COMMENT_READ_ALL_PAGINATION_INVALID("[댓글 전체 조회] - 유효하지 않은 후기 ID", 5040L),
    COMMENT_READ_ALL_PAGINATION_NOT_USER("[댓글 전체 조회] - 해당 후기에 접근 권한 없음", 5041L),

    // 일정 상세 (article_schedule)
    // 일정 상세 - 작성

    ARTICLE_SCHEDULE_POST_INVALID("[일정 상세 - 작성] - 유효하지 않은 초기 여행 계획 ID", 10000L),
    ARTICLE_SCHEDULE_POST_NOT_USER("[일정 상세 - 작성] - 초기 여행 계획 작성자만 상세 여행 계획을 작성할 수 있습니다.", 10001L),
    ARTICLE_SCHEDULE_POST_NOT_DTYPE("[일정 상세] - 올바르지 않은 DTYPE이 포함되어 있습니다.", 10002L),

    // 일정 상세 - 수정
    ARTICLE_SCHEDULE_UPDATE_ARTICLE_INVALID("[일정 상세 - 수정] - 유효하지 않은 초기 여행 계획(article) - ID", 10010L),
    ARTICLE_SCHEDULE_UPDATE_NOT_USER("[일정 상세 - 수정] - 초기 여행 계획 작성자만 상세 여행 계획을 수정할 수 있습니다.", 10011L),
    ARTICLE_SCHEDULE_UPDATE_SCHEDULE_INVALID("[일정 상세 - 수정] - 유효하지 않은 일정(schedule) ID", 10012L),


    // 일정 상세 - 삭제
    ARTICLE_SCHEDULE_DELETE_INVALID("[일정 상세 - 삭제] - 유효하지 않은 초기 여행 계획 - ID", 10020L),
    ARTICLE_SCHEDULE_DELETE_NOT_USER("[일정 상세 - 삭제] - 본인의 초기 여행 계획이 아닙니다.", 10021L),
    // 일정 상세 - 비공개 여부
    ARTICLE_SCHEDULE_PRIVACY_INVALID("[일정 상세 - 비공개 여부] - 유효하지 않은 초기 여행 계획 - ID", 10030L),
    ARTICLE_SCHEDULE_PRIVACY_NOT_USER("[일정 상세 - 비공개 여부] - 본인의 초기 여행 계획이 아닙니다.", 10031L),
    // 일정 상세 - 전체 조회
    ARTICLE_SCHEDULE_READ_DETAIL_INVALID("[일정 상세 - 리스트 조회] - 유효하지 않은 초기 여행 계획 - ID", 10040L),
    ARTICLE_SCHEDULE_READ_DETAIL_NOT_USER("[일정 상세 - 리스트 조회] - 해당 여행 계획에 접근 권한 없음", 10041L),


    // 아티클 관련
    ARTICLE_NOT_FOUND("존재 하지 않는 초기 여행 계획", 6000L),
    ARTICLE_READ_DETAIL_NOT_AUTHORIZATION("[초기 여행 계획 상세 조회] - 해당 여행 계획에 접근 권한 없음", 6010L),
    ARTICLE_DELETE_NOT_USER("[초기 여행 계획 삭제] - 본인의 초기 여행 계획이 아닙니다.", 6020L),
    ARTICLE_UPDATE_NOT_USER("[초기 여행 계획 수정] - 본인의 초기 여행 계획이 아닙니다.", 6030L),

    // Style 관련
    STYLE_NOT_FOUND("존재 하지 않는 스타일", 7000L),

    // Companion 관련
    COMPANION_NOT_FOUND("존재 하지 않는 companion", 8000L),

    // 북마크 관련
    BOOKMARK_NOT_FOUND("존재 하지 않는 북마크", 9000L),

    // 도메인 예외 (domain)
    ARTICLE_VERIFY_OWNER("[여행계획] - 본인의 여행계획이 아닙니다.", 3000L),
    REVIEW_VERIFY_OWNER("[후기] - 본인의 후기가 아닙니다.", 4000L),
    COMMENT_VERIFY_OWNER("[댓글] - 본인의 댓글이 아닙니다.", 5000L),



    BAD_REQUEST("BAD_REQUEST", 9404L),
    BAD_REQUEST_JSON_PARSE_ERROR("[BAD_REQUEST] JSON_PARSE_ERROR - 올바른 JSON 형식이 아님", 9405L),
    // NPE
    NULL_POINT_EXCEPTION("NPE", 9998L),
    // Runtime Exception
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", 9999L);
    public final @NotNull String message;
    public final @NotNull Long code;

    ErrorCodes(String message, Long code) {
        this.message = message;
        this.code = code;
    }
}
