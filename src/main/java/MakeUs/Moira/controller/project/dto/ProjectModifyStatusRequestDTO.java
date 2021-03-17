package MakeUs.Moira.controller.project.dto;

import MakeUs.Moira.domain.project.ProjectStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectModifyStatusRequestDTO {
    private ProjectStatus status;
}
