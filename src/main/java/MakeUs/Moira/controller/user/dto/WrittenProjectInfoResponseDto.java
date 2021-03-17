package MakeUs.Moira.controller.user.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class WrittenProjectInfoResponseDto {
    private String projectTitle;
    private LocalDate writtenTime;
    private int hitCount;
    private String projectImageUrl;
    private int applicantCount;

    @Builder
    public WrittenProjectInfoResponseDto(String projectTitle, LocalDate writtenTime, int hitCount, String projectImageUrl, int applicantCount) {
        this.projectTitle = projectTitle;
        this.writtenTime = writtenTime;
        this.hitCount = hitCount;
        this.projectImageUrl = projectImageUrl;
        this.applicantCount = applicantCount;
    }
}
