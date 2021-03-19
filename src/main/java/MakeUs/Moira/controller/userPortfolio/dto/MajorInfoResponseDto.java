package MakeUs.Moira.controller.userPortfolio.dto;

import MakeUs.Moira.domain.userPortfolio.userSchool.MajorInfo;
import lombok.Getter;

@Getter
public class MajorInfoResponseDto {
    private Long   majorId;
    private String majorName;

    public MajorInfoResponseDto(MajorInfo majorInfo) {
        this.majorId = majorInfo.getId();
        this.majorName = majorInfo.getMajorName();
    }
}
