package MakeUs.Moira.controller.userPortfolio.userLink.dto;


import MakeUs.Moira.domain.userPortfolio.userLink.UserLink;
import lombok.Getter;

@Getter
public class UserLinkAddRequestDto {
    private String linkUrl;

    public UserLink toEntity(){
        return UserLink.builder()
                .linkUrl(this.linkUrl)
                .build();
    }
}
