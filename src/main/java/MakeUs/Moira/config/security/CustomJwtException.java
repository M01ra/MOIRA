package MakeUs.Moira.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomJwtException {

    NO_TOKEN_REQUEST_ERROR (100, "NO_TOKEN_REQUEST"),
    INVALID_SIGN_TOKEN_ERROR (101, "INVALID_SIGN_TOKEN_ERROR"),
    NO_EXPIRATION_ERROR (102, "NO_EXPIRATION_ERROR"),
    EXPIRED_TOKEN_ERROR (103, "TOKEN_EXPIRED, REQUEST_AGAIN_JWT_TOKEN");

    private final int code;
    private final String message;
}
