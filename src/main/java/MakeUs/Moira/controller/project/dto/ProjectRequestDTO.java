package MakeUs.Moira.controller.project.dto;

import MakeUs.Moira.domain.project.projectDetail.ProjectDuration;
import MakeUs.Moira.domain.project.projectDetail.ProjectLocalType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectRequestDTO {
    @ApiModelProperty(value = "제목", example = "웹 프로젝트 팀원 모집합니다")
    private String projectTitle;
    @ApiModelProperty(value = "내용", example = "공공 API를 활용한 웹사이트를 구축할 예정입니다")
    private String projectContent;
    @ApiModelProperty(value = "프로젝트 기간", example = "일주")
    private ProjectDuration projectDuration;
    @ApiModelProperty(value = "지역", example = "서울")
    private ProjectLocalType projectLocalType;
    @ApiModelProperty(value = "포지션 리스트")
    private List<ProjectPositonDTO> projectPositionList;
    @ApiModelProperty(value = "태그 리스트", example = "[\"서버\",\"IOS\",\"AOS\"]")
    private List<String> projectHashtagList;
    @ApiModelProperty(value = "가입질문 리스트", example = "[\"가입하고싶은 동기가 무엇인가요?\",\"한달에 얼마나 참여할 수 있나요?\"]")
    private List<String> projectQuestionList;
}
