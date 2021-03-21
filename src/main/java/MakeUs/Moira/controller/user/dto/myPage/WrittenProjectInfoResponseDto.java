package MakeUs.Moira.controller.user.dto.myPage;


import MakeUs.Moira.domain.project.Project;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WrittenProjectInfoResponseDto {
    private Long      projectId;
    private String    projectTitle;
    private LocalDate writtenTime;
    private int       hitCount;
    private String    projectImageUrl;
    private int       applicantCount;

    public WrittenProjectInfoResponseDto(Project writtenProject) {
        this.projectId = writtenProject.getId();
        this.projectTitle = writtenProject.getProjectTitle();
        this.writtenTime = writtenProject.getCreatedDate()
                                         .toLocalDate();
        this.hitCount = writtenProject.getHitCount();
        this.projectImageUrl = writtenProject.getProjectImageUrl();
        this.applicantCount = writtenProject.getProjectDetail()
                                            .getProjectApplyList()
                                            .size();
    }
}
