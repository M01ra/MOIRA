package MakeUs.Moira.controller.security.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@ApiModel(value = "로그인 입력 폼", description = "Social Login Provider / AccessToken")
@Getter
public class LoginRequestDto {

    @ApiModelProperty(value = "SocialProvider", required = true, allowableValues = "kakao, apple")
    private String socialProvider;

    @ApiModelProperty(value = "AccessToken", required = true)
    private String accessToken;
}
