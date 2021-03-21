package MakeUs.Moira.controller.user.dto.myPageEdit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;


@Getter
@ApiModel
public class MyPageEditProfileUpdateRequestDto {

    @ApiModelProperty(value="닉네임")
    private String nickname;

    @ApiModelProperty(value="포지션 Id")
    private Long positionId;

    @ApiModelProperty(value="한 줄 소개")
    private String shortIntroduction;

    @ApiModelProperty(value="관심태그 id 목록")
    private List<Long> hashtagIdList;
}
