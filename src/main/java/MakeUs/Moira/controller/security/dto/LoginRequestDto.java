package MakeUs.Moira.controller.security.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "로그인 입력 폼", description = "Social Login Provider / AccessToken")
@ToString
@Getter
public class LoginRequestDto {

    @ApiModelProperty(value = "SocialProvider", required = true, allowableValues = "kakao, apple")
    @NotBlank(message = "socialProvider에 빈 값을 넣을 수 없음")
    private String socialProvider;

    @ApiModelProperty(value = "AccessToken", required = true)
    @NotBlank(message = "accessToken에 빈 값을 넣을 수 없음")
    private String accessToken;
}
