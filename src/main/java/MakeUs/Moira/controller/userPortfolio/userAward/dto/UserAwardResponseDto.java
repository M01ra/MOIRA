package MakeUs.Moira.controller.userPortfolio.userAward.dto;

import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserAwardResponseDto {
    private Long userAwardId;
    private String userAwardName;
    private String userAwardContent;

    public UserAwardResponseDto(UserAward userAward) {
        this.userAwardId = userAward.getId();
        this.userAwardName = userAward.getAwardName();
        this.userAwardContent = userAward.getAwardContent();
    }
}
