package MakeUs.Moira.controller.userReview.dto;

import MakeUs.Moira.domain.userReview.UserReviewComplimentMark;
import lombok.Getter;

@Getter
public class ComplimentMarkDto {
    private Long   complimentMarkId;
    private String complimentMarkName;

    public ComplimentMarkDto(UserReviewComplimentMark userReviewComplimentMark) {
        this.complimentMarkId = userReviewComplimentMark.getComplimentMarkInfo()
                                                        .getId();
        this.complimentMarkName = userReviewComplimentMark.getComplimentMarkInfo()
                                                          .getMarkName();
    }
}
