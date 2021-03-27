package MakeUs.Moira.controller.userReview.dto;

import MakeUs.Moira.domain.userReview.UserReview;
import lombok.Getter;

import java.util.List;

@Getter
public class UserReviewAddRequestDto {
    private Long userProjectId;
    private int mannerPoint;
    private String reviewContent;
    private List<Long> complimentMarkIdList;

    public UserReview toEntity(){
        return UserReview.builder()
                         .mannerPoint(this.mannerPoint)
                         .reviewContent(this.reviewContent)
                         .build();
    }
}