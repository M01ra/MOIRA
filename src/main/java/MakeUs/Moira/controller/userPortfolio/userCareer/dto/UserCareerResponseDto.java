package MakeUs.Moira.controller.userPortfolio.userCareer.dto;


import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareer;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserCareerResponseDto {
    private Long      userCareerId;
    private String    companyName;
    private String    task;
    private LocalDate startAt;
    private LocalDate endAt;

    public UserCareerResponseDto(UserCareer userCareer) {
        this.userCareerId = userCareer.getId();
        this.companyName = userCareer.getCompanyName();
        this.task = userCareer.getTask();
        this.startAt = userCareer.getStartAt();
        this.endAt = userCareer.getEndAt();
    }
}
