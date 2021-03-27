package MakeUs.Moira.controller.userPortfolio.userAward.dto;

import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class UserAwardAddRequestDto {

    @NotBlank(message = "awardName에 빈 값을 넣을 수 없음")
    private String awardName;

    @NotBlank(message = "awardContent에 빈 값을 넣을 수 없음")
    private String awardContent;

    public UserAward toEntity() {
        return UserAward.builder()
                        .awardName(this.awardName)
                        .awardContent(this.awardContent)
                        .build();
    }
}
