package MakeUs.Moira.controller.project.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import jdk.vm.ci.meta.Local;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectsResponseDTO {
    @ApiModelProperty(value = "프로젝트 ID", example = "1")
    private Long id;
    @ApiModelProperty(value = "글쓴이", example = "웰시고기")
    private String writer;
    @ApiModelProperty(value = "제목", example = "웹 프로젝트 팀원 모집합니다")
    private String title;
    @ApiModelProperty(value = "태그 리스트", example = "[\"서버\",\"IOS\",\"AOS\"]")
    private List<String> hashtagList;
    @ApiModelProperty(value = "이미지 URL", example = "https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png")
    private String imageUrl;
    @ApiModelProperty(value = "조회수", example = "3")
    private int hitCount;
    @ApiModelProperty(value = "생성 시간", example = "방금전")
    private String time;
}
