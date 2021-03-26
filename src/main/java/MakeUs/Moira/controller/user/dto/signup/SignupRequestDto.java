package MakeUs.Moira.controller.user.dto.signup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "회원 가입 입력 폼", description = "닉네임 / 상세 포지션 id / 관심태그 id의 리스트")
@Getter
@ToString
public class SignupRequestDto {

    @ApiModelProperty(value = "닉네임", required = true)
    @NotBlank(message = "nickname에 빈 값을 넣을 수 없음")
    private String nickname;

    @ApiModelProperty(value = "상세 포지션", required = true)
    @NotNull(message = "positionId에 빈 값을 넣을 수 없음")
    private Long positionId;

    @ApiModelProperty(value = "관심 태그 id", required = true)
    private List<Long> hashtagIdList;
}
