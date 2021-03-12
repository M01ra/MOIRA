package MakeUs.Moira.controller.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCommentRequestDTO {
    @ApiModelProperty(value = "댓글 내용", example = "언제쯤부터 프로젝트 시작하나요?")
    private String content;
}
