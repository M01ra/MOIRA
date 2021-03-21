package MakeUs.Moira.controller.userPortfolio.userCareer.dto;


import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareer;
import lombok.Getter;

@Getter
public class UserCareerAddRequestDto {
    private String companyName;
    private String task;
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
