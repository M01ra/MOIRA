package MakeUs.Moira.controller.review.dto;

import MakeUs.Moira.domain.userReview.UserReviewComplimentMark;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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
