package MakeUs.Moira.controller.project.dto.myProject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MyProjectResponseDTO {
    @ApiModelProperty(value = "제목", example = "웹 프로젝트 팀원 모집합니다")
    private String title;
    @ApiModelProperty(value = "이미지 URL", example = "https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png")
    private String imageUrl;
    @ApiModelProperty(value = "내용", example = "안녕하세요")
    private String content;
    @ApiModelProperty(value = "팀원 수", example = "4")
    private int memberCount;
    @ApiModelProperty(value = "팀원 리스트")
    private List<MyProjectTeammateResponseDTO> myProjectTeammateResponseDTOList;
}
