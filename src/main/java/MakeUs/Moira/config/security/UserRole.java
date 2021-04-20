package MakeUs.Moira.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("ROLE_USER", "로그인_사용자");

    private final String key;
    private final String title;
}
