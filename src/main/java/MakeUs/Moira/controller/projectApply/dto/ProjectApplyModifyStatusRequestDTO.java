package MakeUs.Moira.controller.projectApply.dto;

import MakeUs.Moira.domain.projectApply.ProjectApplyStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ProjectApplyModifyStatusRequestDTO {
    @ApiModelProperty(value = "변경할 상태", example = "USER_ACCEPTED")
    @NotNull(message = "status에 빈 값을 넣을 수 없음")
    private ProjectApplyStatus status;
}
