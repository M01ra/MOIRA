package MakeUs.Moira.controller.user.dto.myPage;


import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.project.projectApply.ProjectApply;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class AppliedProjectInfoResponseDto {
    private Long      projectId;
    private Long      projectApplyId;
    private String    projectTitle;
    private LocalDate writtenTime;
    private int       hitCount;
    private String    projectImageUrl;

    public AppliedProjectInfoResponseDto(ProjectApply projectApply)
    {
        ProjectDetail projectDetail = projectApply.getProjectDetail();
        this.projectId = projectDetail
                .getProject()
                .getId();
        this.projectApplyId = projectApply.getId();
        this.projectTitle = projectDetail
                .getProject()
                .getProjectTitle();
        this.writtenTime = projectDetail
                .getProject()
                .getCreatedDate()
                .toLocalDate();
        this.hitCount = projectDetail
                .getProject()
                .getHitCount();
        this.projectImageUrl = projectDetail
                .getProject()
                .getProjectImageUrl();
    }
}
