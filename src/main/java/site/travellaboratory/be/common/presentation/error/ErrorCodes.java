package site.travellaboratory.be.common.presentation.error;

public enum ErrorCodes {

    // BeApplicationResponse
    OK("성공", 200L),

    // 회원가입
    AUTH_USER_NOT_IS_AGREEMENT("개인정보 수집 미동의", 1000L),
    AUTH_DUPLICATED_USER_NAME("이미 존재하는 이메일입니다.", 1001L),
    AUTH_DUPLICATED_NICK_NAME("중복된 닉네임입니다.", 1002L),
    // 로그인
    LOGIN_USERNAME_NOT_FOUND("[로그인] - 사용자를 찾을 수 없습니다.", 1003L),
    AUTH_INVALID_PASSWORD("[로그인] - 잘못된 비밀번호입니다.", 1004L),

    AUTH_USER_NOT_FOUND("사용자를 찾을 수 없습니다.", 1010L),

    // todo : tokenErrorCode 로 분리
    TOKEN_INVALID_TOKEN("유효하지 않은 액세스 토큰", 2000L),
    TOKEN_EXPIRED_TOKEN("만료된 액세스 토큰", 2001L),
    TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND("액세스 토큰이 존재하지 않음", 2002L),
    TOKEN_AUTHORIZATION_FAIL("액세스 토큰 관련 알 수 없는 에러로 인증 실패", 2003L),
    REFRESH_TOKEN_INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰", 2004L),
    TOKEN_NOT_EXPIRED_ACCESS_TOKEN("[토큰 재발급] 만료되지 않은 액세스 토큰", 2005L),
    REFRESH_TOKEN_EXPIRED_TOKEN("만료된 리프레시 토큰 - 재로그인", 2008L),
    REFRESH_TOKEN_TOKEN_EXCEPTION("리프레시 토큰 관련 알 수 없는 에러", 2009L),

    // 비밀번호 질문, 답 관련
    PASSWORD_INVALID_QUESTION("유효하지 않은 질문", 2010L),

    // 비밀번호 문의 - 답변
    PASSWORD_INVALID_EMAIL("유효하지 않은 이메일", 2011L),
    PASSWORD_INQUIRY_INVALID_ANSWER("올바르지 않은 답변", 2012L),

    // 유저 관련
    USER_NOT_FOUND("존재하지 않는 유저", 3000L),
    PROFILE_DUPLICATED_NICK_NAME("[프로필] 중복된 닉네임입니다.", 3001L),

    // 후기 (review)
    REVIEW_VERIFY_OWNER("[후기] - 본인의 후기가 아닙니다.", 4000L),
    REVIEW_INVALID_ARTICLE_ID("[후기] - 유효하지 않은 여행 계획[Article] ID", 4001L),
    REVIEW_INVALID_REVIEW_ID("[후기] - 유효하지 않은 후기[Review] ID", 4002L),
    REVIEW_POST_EXIST("[후기] - 각 여행 계획에 대한 후기는 한 개만 작성할 수 있습니다.", 4003L),

    // 후기 좋아요
    REVIEW_LIKE_INVALID_REVIEW_ID("[후기 좋아요] - 유효하지 않은 후기 ID", 4100L),
    // 후기 상세 조회
    REVIEW_READ_DETAIL_INVALID("[후기 상세 조회] - 유효하지 않은 후기 ID", 4040L),
    // 후기 작성 전 조회
    REVIEW_BEFORE_POST_INVALID("[후기 작성 전 조회] - 유효하지 않은 여행 계획 ID", 4050L),
    REVIEW_BEFORE_POST_NOT_USER("[후기 작성 전 조회] - 여행 계획 작성자만 해당 여행 계획의 후기를 작성할 수 있습니다.", 4051L),
    REVIEW_BEFORE_POST_EXIST("[후기 작성 전 조회] - 각 여행 계획에 대한 후기는 한 개만 작성할 수 있습니다.", 4052L),
    REVIEW_BEFORE_POST_NOT_EXIST_SCHEDULES("[후기 작성 전 조회] - 상세 일정 작성 후 후기 작성이 가능합니다.", 4053L),
    // 프로필 - 리뷰 전체 조회 (페이지네이션)
    PROFILE_REVIEW_READ_USER_NOT_FOUND("[프로필 - 후기 전체 조회] - 유효하지 않은 User ID", 4060L),

    // 댓글
    COMMENT_VERIFY_OWNER("[댓글] - 본인의 댓글이 아닙니다.", 5000L),
    COMMENT_INVALID_REVIEW_ID("[댓글] - 유효하지 않은 후기[Review] ID", 5001L),
    COMMENT_INVALID_COMMENT_ID("[댓글] - 유효하지 않은 댓글[Comment] ID", 5002L),

    // 댓글 좋아요
    COMMENT_LIKE_INVALID_COMMENT_ID("[댓글 좋아요] - 유효하지 않은 댓글 ID", 5100L),
    // 댓글 전체 조회 (페이지네이션)
    COMMENT_READ_ALL_PAGINATION_INVALID("[댓글 전체 조회] - 유효하지 않은 후기 ID", 5040L),

    // 일정 상세 (article_schedule)
    // 일정 상세 - 작성
    ARTICLE_SCHEDULE_POST_NOT_DTYPE("[일정 상세] - 올바르지 않은 DTYPE이 포함되어 있습니다.", 10002L),

    // 일정 상세 - 수정
    ARTICLE_SCHEDULE_UPDATE_ARTICLE_INVALID("[일정 상세 - 수정] - 유효하지 않은 초기 여행 계획(article) - ID", 10010L),
    ARTICLE_SCHEDULE_UPDATE_SCHEDULE_INVALID("[일정 상세 - 수정] - 유효하지 않은 일정(schedule) ID", 10012L),

    // 일정 상세 - 삭제
    ARTICLE_SCHEDULE_DELETE_INVALID("[일정 상세 - 삭제] - 유효하지 않은 초기 여행 계획 - ID", 10020L),

    // 일정 상세 - 전체 조회
    ARTICLE_SCHEDULE_READ_DETAIL_INVALID("[일정 상세 - 리스트 조회] - 유효하지 않은 초기 여행 계획 - ID", 10040L),
    ARTICLE_SCHEDULE_READ_DETAIL_NOT_USER("[일정 상세 - 리스트 조회] - 해당 여행 계획에 접근 권한 없음", 10041L),

    // 아티클 관련
    ARTICLE_NOT_FOUND("존재 하지 않는 초기 여행 계획", 6000L),
    ARTICLE_VERIFY_OWNER("본인의 여행계획이 아닙니다.", 6001L),

    // Style 관련
    STYLE_NOT_FOUND("존재 하지 않는 스타일", 7000L),

    // Companion 관련
    COMPANION_NOT_FOUND("존재 하지 않는 companion", 8000L),

    // 북마크 관련
    BOOKMARK_NOT_FOUND("존재 하지 않는 북마크", 9000L),

    // 여행계획


    BAD_REQUEST("BAD_REQUEST", 9404L),
    BAD_REQUEST_JSON_PARSE_ERROR("[BAD_REQUEST] JSON_PARSE_ERROR - 올바른 JSON 형식이 아님", 9405L),
    BAD_REQUEST_REQUEST_ATTRIBUTES_MISSING("[BAD_REQUEST] RequestAttributes is null, Cannot resolve userId.", 9406L),
    BAD_REQUEST_USER_ID_MISSING("[BAD_REQUEST] userId attribute is not set in the RequestAttributes.", 9407L),
    BAD_REQUEST_NOT_REQUEST_MULTIPART("[BAD_REQUEST] Multipart request is required but was not provided. - 파일을 보내지 않았습니다.", 9408L),
    // S3
    FILE_UPLOAD_FAILED("S3에 파일 업로드 실패", 9997L),
    // NPE
    NULL_POINT_EXCEPTION("NPE", 9998L),
    // Runtime Exception
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", 9999L),

    // FILE_UPLOAD_COMMON 100,000
    FILE_IS_EMPTY("파일이 비어 있습니다.", 100001L),
    FILE_NAME_EMPTY("파일 이름이 비어 있습니다.", 100002L),
    FILE_SIZE_ZERO("파일 크기가 0입니다.", 100003L),
    NOT_EXIST_FILE_FORMAT("파일 형식이 존재하지 않습니다. 올바른 파일을 업로드해 주세요.", 100004L),
    INVALID_FILE_FORMAT("지원하지 않는 파일 형식입니다. 다른 파일 형식으로 업로드해 주세요.", 100005L),
    IMAGE_RESIZING_AND_COMPRESS_FAILED("이미지 파일을 리사이징 혹은 압축 실패하였습니다.", 100006L);
    ;

    public final String message;
    public final Long code;

    ErrorCodes(String message, Long code) {
        this.message = message;
        this.code = code;
    }
}
