package MakeUs.Moira.controller.project.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectCommentResponseDTO {
    @ApiModelProperty(value = "댓글 ID", example = "2")
    private Long id;
    @ApiModelProperty(value = "글쓴이 ID", example = "웰시고기")
    private Long userId;
    @ApiModelProperty(value = "부모 댓글 ID", example = "1")
    private Long parentId;
    @ApiModelProperty(value = "글쓴이 닉네임", example = "웰시고기")
    private String nickname;
    @ApiModelProperty(value = "글쓴이 이미지 URL", example = "https://moira-springboot.s3.ap-northeast-2.amazonaws.com/project-3-Sketchpad.png")
    private String imageUrl;
    @ApiModelProperty(value = "댓글 내용", example = "안녕하세요")
    private String content;
    @ApiModelProperty(value = "생성 시간", example = "방금전")
    private String time;
    @ApiModelProperty(value = "삭제 가능 여부(로그인한 유저가 쓴 댓글일 경우)", example = "false")
    private boolean isDeletable;
}
