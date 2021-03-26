package MakeUs.Moira.controller.user.dto.myPage;


import MakeUs.Moira.domain.project.Project;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class AppliedProjectInfoResponseDto {
    private Long      projectId;
    private String    projectTitle;
    private LocalDate writtenTime;
    private int       hitCount;
    private String    projectImageUrl;

    public AppliedProjectInfoResponseDto(Project appliedProject) {
        this.projectId = appliedProject.getId();
        this.projectTitle = appliedProject.getProjectTitle();
        this.writtenTime = appliedProject.getCreatedDate()
                                         .toLocalDate();
        this.hitCount = appliedProject.getHitCount();
        this.projectImageUrl = appliedProject.getProjectImageUrl();
    }
}
