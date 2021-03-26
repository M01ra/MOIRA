package MakeUs.Moira.controller.project.dto.projectComment;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ProjectCommentRequestDTO {
    @ApiModelProperty(value = "댓글 내용", example = "언제쯤부터 프로젝트 시작하나요?")
    @NotNull(message = "content에 빈 값을 넣을 수 없음")
    private String content;
}
