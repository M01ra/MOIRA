package MakeUs.Moira.controller.project.dto.myProject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyProjectTeammateResponseDTO {
    @ApiModelProperty(value = "유저 ID", example = "1")
    private Long userId;
    @ApiModelProperty(value = "닉네임", example = "웰시고기")
    private String nickname;
    @ApiModelProperty(value = "이미지 URL", example = "https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png")
    private String imageUrl;
    @ApiModelProperty(value = "포지션", example = "IOS")
    private String position;
    @ApiModelProperty(value = "리더 유무", example = "true")
    private boolean isLeader;
}
