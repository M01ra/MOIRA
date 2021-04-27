package MakeUs.Moira.controller.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProjectModifyRequestDTO {
    @ApiModelProperty(value = "변경할 제목", example = "VR 팀원 모집합니다")
    @NotBlank(message = "title에 빈 값을 넣을 수 없음")
    private String title;

    @ApiModelProperty(value = "변경할 내용", example = "아무나 오세요")
    @NotBlank(message = "content에 빈 값을 넣을 수 없음")
    private String content;
}
