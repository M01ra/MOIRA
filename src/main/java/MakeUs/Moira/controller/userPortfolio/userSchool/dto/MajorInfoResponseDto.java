package MakeUs.Moira.controller.userPortfolio.userSchool.dto;

import MakeUs.Moira.domain.userPortfolio.userSchool.MajorInfo;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MajorInfoResponseDto {
    private Long   majorId;
    private String majorName;

    public MajorInfoResponseDto(MajorInfo majorInfo) {
        this.majorId = majorInfo.getId();
        this.majorName = majorInfo.getMajorName();
    }
}
