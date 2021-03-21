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
public class ProjectApplysResponseDTO {
    @ApiModelProperty(value = "프로젝트 지원 ID", example = "1")
    private Long projectApplyId;
    @ApiModelProperty(value = "프로젝트 ID", example = "1")
    private Long projectId;
    @ApiModelProperty(value = "프로젝트 이미지 URL", example = "https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png")
    private String imageUrl;
    @ApiModelProperty(value = "프로젝트 이름", example = "웹 개발 프로젝트")
    private String title;
    @ApiModelProperty(value = "조회수", example = "7")
    private int hitCount;
    @ApiModelProperty(value = "시간", example = "방금 전")
    private String time;
}
