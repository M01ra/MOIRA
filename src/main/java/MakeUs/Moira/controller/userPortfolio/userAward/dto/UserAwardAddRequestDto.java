package MakeUs.Moira.controller.userPortfolio.userAward.dto;

import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import lombok.Getter;

@Getter
public class UserAwardAddRequestDto {
    private String awardName;
    private String awardContent;

    public UserAward toEntity() {
        return UserAward.builder()
                        .awardName(this.awardName)
                        .awardContent(this.awardContent)
                        .build();
    }
}
