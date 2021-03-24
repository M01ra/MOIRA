package MakeUs.Moira.controller.project.dto.projectApply;

import MakeUs.Moira.domain.project.projectApply.ProjectApplyStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectApplyModifyStatusRequestDTO {
    @ApiModelProperty(value = "변경할 상태", example = "ACCEPT")
    private ProjectApplyStatus status;
}
