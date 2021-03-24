package MakeUs.Moira.controller.userPool;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserPoolDetailReviewDetailResponseDto {
    private String    userProfileUrl;
    private String    nickname;
    private int       mannerPoint;
    private String    reviewContent;
    private LocalDate writtenDate;

    @Builder
    public UserPoolDetailReviewDetailResponseDto(String userProfileUrl,
                                                 String nickname,
                                                 int mannerPoint,
                                                 String reviewContent, LocalDate writtenDate)
    {
        this.userProfileUrl = userProfileUrl;
        this.nickname = nickname;
        this.mannerPoint = mannerPoint;
        this.reviewContent = reviewContent;
        this.writtenDate = writtenDate;
    }
}
