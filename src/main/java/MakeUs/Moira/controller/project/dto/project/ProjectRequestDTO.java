package MakeUs.Moira.controller.project.dto.project;

import MakeUs.Moira.domain.project.projectDetail.ProjectDuration;
import MakeUs.Moira.domain.project.projectDetail.ProjectLocalType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProjectRequestDTO {
    @ApiModelProperty(value = "제목", example = "웹 프로젝트 팀원 모집합니다")
    @NotBlank(message = "title에 빈 값을 넣을 수 없음")
    private String title;

    @NotNull(message = "content에 빈 값을 넣을 수 없음")
    @ApiModelProperty(value = "내용", example = "공공 API를 활용한 웹사이트를 구축할 예정입니다")
    private String content;

    @ApiModelProperty(value = "프로젝트 기간", example = "한달_미만", allowableValues = "한달_미만, 세달_미만, 여섯달_미만, 여섯달_이상")
    @NotNull(message = "duration에 빈 값을 넣을 수 없음")
    private ProjectDuration duration;

    @ApiModelProperty(value = "지역", example = "서울_인천_경기", allowableValues = "서울_인천_경기, 대전_충북_충남_세종, 광주_전남_전북, 부산_울산_경남, 강원, 대구_경북, 제주, 온라인")
    @NotNull(message = "localType에 빈 값을 넣을 수 없음")
    private ProjectLocalType localType;

    @ApiModelProperty(value = "포지션 카테고리 리스트")
    private List<ProjectPositionCategoryDTO> positionCategoryList;

    @ApiModelProperty(value = "태그 리스트", example = "[\"서버\",\"IOS\",\"AOS\"]")
    private List<String> hashtagList;
}
