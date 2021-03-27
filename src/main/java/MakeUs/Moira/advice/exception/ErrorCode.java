package MakeUs.Moira.advice.exception;

public enum ErrorCode {

    NOT_NULL(400, ""),
    NOT_BLANK(401, ""),
    MIN(402, ""),
    MAX(403, ""),

    // 410 : HttpMessageNotReadableException -> enum 에러
    // 411 : HttpClientErrorException -> 잘못된 kakao 토큰


    INVALID_USER(420, "유효하지 않은 유저 혹은 유저 ID"),
    INVALID_PROJECT(421, "유효하지 않은 프로젝트 혹은 프로젝트 ID"),
    INVALID_COMMENT(422, "유효하지 않은 댓글 혹은 댓글 ID"),
    INVALID_PROJECT_APPLY(423, "유효하지 않은 지원서 혹은 지원서 ID"),
    INVALID_USER_POOL(424, "유효하지 않은 유저풀 혹은 유저풀 ID"),
    INVALID_USER_PROJECT(425, "유효하지 않은 유저 프로젝트 혹은 유저 프로젝트 ID"),
    INVALID_PROJECT_APPLY_OPTIONAL_INFO(426, "유효하지 않은 지원서 선택사항 정보"),
    INVALID_SCHOOL(427, "유효하지 않은 학교 혹은 학교 ID"),
    INVALID_MAJOR(428, "유효하지 않은 전공 혹은 전공 ID"),
    INVALID_POSITION(429, "유효하지 않은 포지션 혹은 포지션 ID"),
    INVALID_POSITION_CATEGORY(430, "유효하지 않은 포지션 카테고리 혹은 ID"),
    INVALID_HASHTAG(431, "유효하지 않은 태그 이름 혹은 ID"),
    INVALID_SORT(432, "유효하지 않은 정렬 방식"),
    INVALID_FILE_IO(433, "유효하지 않은 파일 입출력"),
    INVALID_TOKEN_PROVIDER(434, "유효하지 않은 토큰 제공자(kakao)"),
    INVALID_JWT_TOKEN(435, "유효하지 않은 JWT 토큰"),
    INVALID_MESSAGE_TYPE(436, "유효하지 않은 쪽지 타입"),
    INVALID_CHAT_ROOM(436, "유효하지 않은 채팅방"),

    ALREADY_REGISTRED_USER(450, "이미 가입된 유저"),
    ALREADY_REGISTRED_NICKNAME(451, "이미 존재하는 닉네임"),

    UNAUTHORIZED_USER(460, "권한이 없는 유저"),
    UNAUTHORIZED_JWT(461, "해당 리소스에 접근 권한이 없는 JWT"),

    NON_EXIST_FILE_NAME(470, "존재하지 않는 파일 이름");



    private final int code;
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ErrorCode(final int code, final String message) {
        this.message = message;
        this.code = code;
    }
}