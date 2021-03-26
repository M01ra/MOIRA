package MakeUs.Moira.controller.userPortfolio.userLink.dto;


import MakeUs.Moira.domain.userPortfolio.userLink.UserLink;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class UserLinkAddRequestDto {

    @NotBlank(message = "linkUrl에 빈 값을 넣을 수 없음")
    private String linkUrl;

    public UserLink toEntity(){
        return UserLink.builder()
                .linkUrl(this.linkUrl)
                .build();
    }
}
