package MakeUs.Moira.controller.myPage.dto;

import MakeUs.Moira.domain.projectApply.ProjectApply;
import MakeUs.Moira.domain.projectDetail.ProjectDetail;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@ToString
public class AppliedProjectInfoResponseDto {
    private Long      projectId;
    private Long      projectApplyId;
    private String    projectTitle;
    private String    writtenTime;
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
        this.writtenTime = getTime(projectDetail
                .getProject()
                .getCreatedDate());
        this.hitCount = projectDetail
                .getProject()
                .getHitCount();
        this.projectImageUrl = projectDetail
                .getProject()
                .getProjectImageUrl();
    }

    private String getTime(LocalDateTime localDateTime){
        String time;
        if(ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now())) + "년 전";
        }
        else if(ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now())) + "개월 전";
        }
        else if(ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now())) + "일 전";
        }
        else if(ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now())) + "시간 전";
        }
        else if(ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now())) + "분 전";
        }
        else {
            time = "방금 전";
        }
        return time;
    }
}
