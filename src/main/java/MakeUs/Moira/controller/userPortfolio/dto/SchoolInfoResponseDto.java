package MakeUs.Moira.controller.userPortfolio.dto;

import MakeUs.Moira.domain.userPortfolio.userSchool.SchoolInfo;
import lombok.Getter;


@Getter
public class SchoolInfoResponseDto {
    private Long   schoolId;
    private String schoolName;

    public SchoolInfoResponseDto(SchoolInfo schoolInfo) {
        this.schoolId = schoolInfo.getId();
        this.schoolName = schoolInfo.getSchoolName();
    }
}
