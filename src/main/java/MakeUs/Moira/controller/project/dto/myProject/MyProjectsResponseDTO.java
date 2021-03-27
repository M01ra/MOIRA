package MakeUs.Moira.controller.project.dto.myProject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MyProjectsResponseDTO {
    @ApiModelProperty(value = "프로젝트 ID", example = "1")
    private Long projectId;
    @ApiModelProperty(value = "제목", example = "모이라")
    private String title;
    @ApiModelProperty(value = "이미지 URL", example = "https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png")
    private String imageUrl;
    @ApiModelProperty(value = "멤버 수", example = "5")
    private int memberCount;
    @JsonIgnore
    private LocalDateTime date;
}
