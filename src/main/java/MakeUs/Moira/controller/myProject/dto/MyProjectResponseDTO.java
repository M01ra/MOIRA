package MakeUs.Moira.controller.myProject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyProjectResponseDTO {
    @ApiModelProperty(value = "제목", example = "웹 프로젝트 팀원 모집합니다")
    private String title;
    @ApiModelProperty(value = "이미지 URL 리스트", example = "[\"https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png\"]")
    private List<String> imageUrlList;
    @ApiModelProperty(value = "내용", example = "안녕하세요")
    private String content;
    @ApiModelProperty(value = "팀원 수", example = "4")
    private int memberCount;
    @ApiModelProperty(value = "팀장인지 여부", example = "true")
    private boolean isLeader;
    @ApiModelProperty(value = "팀원 리스트")
    private List<MyProjectTeammateResponseDTO> myProjectTeammateResponseDTOList;
}
