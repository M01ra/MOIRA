package MakeUs.Moira.controller.project.dto;

import MakeUs.Moira.domain.projectDetail.ProjectDuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponseDTO {
    @ApiModelProperty(value = "글쓴이", example = "웰시고기")
    private String writer;
    @ApiModelProperty(value = "제목", example = "웹 프로젝트 팀원 모집합니다")
    private String title;
    @ApiModelProperty(value = "내용", example = "안녕하세요")
    private String content;
    @ApiModelProperty(value = "태그 리스트", example = "[\"서버\",\"IOS\",\"AOS\"]")
    private List<String> hashtagList;
    @ApiModelProperty(value = "이미지 URL 리스트", example = "[\"https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png\"]")
    private List<String> imageUrlList;
    @ApiModelProperty(value = "조회수", example = "3")
    private int hitCount;
    @ApiModelProperty(value = "좋아요수", example = "2")
    private int likeCount;
    @ApiModelProperty(value = "기간", example = "한달_미만")
    private ProjectDuration duration;
    @ApiModelProperty(value = "포지션 리스트")
    private List<ProjectPositionCategoryDTO> positionCategoryList;
    @ApiModelProperty(value = "생성 시간", example = "방금전")
    private String time;
    @ApiModelProperty(value = "좋아요 여부", example = "true")
    @JsonProperty("isLike")
    private boolean isLike;
}
