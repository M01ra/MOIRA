package MakeUs.Moira.controller.project.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectPositonDTO {
    @ApiModelProperty(value = "포지션 이름", example = "서버")
    private String positionName;
    @ApiModelProperty(value = "인원 수", example = "3")
    private int count;
}
