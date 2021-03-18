package MakeUs.Moira.controller.security.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {

    private String  jwtToken;
    private boolean isFirstLogin;

    public LoginResponseDto(String jwtToken, boolean isFirstLogin) {
        this.jwtToken = jwtToken;
        this.isFirstLogin = isFirstLogin;
    }
}
