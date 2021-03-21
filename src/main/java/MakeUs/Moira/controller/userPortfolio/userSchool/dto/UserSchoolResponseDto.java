package MakeUs.Moira.controller.userPortfolio.userSchool.dto;

import MakeUs.Moira.domain.userPortfolio.userSchool.UserSchool;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSchoolResponseDto {

    private Long      userSchoolId;
    private String    schoolName;
    private String    majorName;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private String    schoolStatus;

    public UserSchoolResponseDto(UserSchool userSchool) {
        this.userSchoolId = userSchool.getId();
        this.schoolName = userSchool.getSchoolInfo()
                                    .getSchoolName();
        this.majorName = userSchool.getMajorInfo()
                                   .getMajorName();
        this.startedAt = userSchool.getStartAt();
        this.endedAt = userSchool.getEndAt();
        this.schoolStatus = userSchool.getUserSchoolStatus()
                                      .name();
    }
}
