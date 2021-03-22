package MakeUs.Moira.controller.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectApplicantsResponseDTO {
    @ApiModelProperty(value = "프로젝트 지원 ID", example = "1")
    private Long projectApplyId;
    @ApiModelProperty(value = "지원자 프로필 URL", example = "https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png")
    private String imageUrl;
    @ApiModelProperty(value = "닉네임", example = "웰시고기")
    private String nickname;
    @ApiModelProperty(value = "지원 포지션", example = "IOS")
    private String position;
}
