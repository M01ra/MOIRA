package MakeUs.Moira.controller.userReview.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class UserReviewDetailResponseDto {
    private String    userProfileUrl;
    private String    nickname;
    private int       mannerPoint;
    private String    reviewContent;
    private LocalDate writtenDate;

    @Builder
    public UserReviewDetailResponseDto(String userProfileUrl,
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
