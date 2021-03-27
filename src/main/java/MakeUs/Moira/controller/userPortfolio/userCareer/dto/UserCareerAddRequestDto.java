package MakeUs.Moira.controller.userPortfolio.userCareer.dto;


import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareer;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class UserCareerAddRequestDto {
    @NotBlank(message = "awardName에 빈 값을 넣을 수 없음")
    private String companyName;

    @NotBlank(message = "task에 빈 값을 넣을 수 없음")
    private String task;

    @NotBlank(message = "startAt에 빈 값을 넣을 수 없음")
    private String startAt;

    private String endAt;

    public UserCareer toEntity() {
        return UserCareer.builder()
                         .companyName(this.companyName)
                         .task(this.companyName)
                         .startAt(this.startAt)
                         .endAt(this.endAt)
                         .build();
    }
}
