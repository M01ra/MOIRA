package MakeUs.Moira.controller.security.dto;

import lombok.*;

@Getter
@ToString
public class LoginResponseDto {

    private String  jwtToken;
    private boolean needSignup;

    @Builder
    public LoginResponseDto(String jwtToken, boolean needSignup) {
        this.jwtToken = jwtToken;
        this.needSignup = needSignup;
    }

    public static LoginResponseDto of(String jwtToken, boolean needSignup) {
        return LoginResponseDto.builder()
                               .jwtToken(jwtToken)
                               .needSignup(needSignup)
                               .build();
    }
}
