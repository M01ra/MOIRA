package MakeUs.Moira.controller.project.dto.project;

import MakeUs.Moira.domain.project.ProjectStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProjectModifyStatusRequestDTO {
    @ApiModelProperty(value = "변경할 상태", example = "RECRUITING", allowableValues = "RECRUITING, PROGRESSING, COMPLETED")
    @NotNull(message = "status에 빈 값을 넣을 수 없음")
    private ProjectStatus status;
}
