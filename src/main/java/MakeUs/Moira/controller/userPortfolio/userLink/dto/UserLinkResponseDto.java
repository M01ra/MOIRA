package MakeUs.Moira.controller.userPortfolio.userLink.dto;

import MakeUs.Moira.domain.userPortfolio.userLink.UserLink;
import lombok.Getter;

@Getter
public class UserLinkResponseDto {
    private Long userLinkId;
    private String userLinkUrl;

    public UserLinkResponseDto(UserLink userLink) {
        this.userLinkId = userLink.getId();
        this.userLinkUrl = userLink.getLinkUrl();
    }
}
