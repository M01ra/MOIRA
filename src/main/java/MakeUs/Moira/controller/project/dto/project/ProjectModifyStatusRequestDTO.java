package MakeUs.Moira.controller.project.dto.project;

import MakeUs.Moira.domain.project.ProjectStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectModifyStatusRequestDTO {
    @ApiModelProperty(value = "변경할 상태", example = "IOS")
    private ProjectStatus status;
}
