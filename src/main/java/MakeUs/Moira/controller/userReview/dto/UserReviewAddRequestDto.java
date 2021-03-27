package MakeUs.Moira.controller.userReview.dto;

import MakeUs.Moira.domain.userReview.UserReview;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@ToString
public class UserReviewAddRequestDto {

    @NotNull(message = "userProjectId에 빈 값을 넣을 수 없음")
    private Long userProjectId;

    @NotNull(message = "mannerPoint에 빈 값을 넣을 수 없음")
    private int mannerPoint;

    @NotBlank(message = "reviewContent에 빈 값을 넣을 수 없음")
    private String reviewContent;

    private List<Long> complimentMarkIdList;

    public UserReview toEntity(){
        return UserReview.builder()
                         .mannerPoint(this.mannerPoint)
                         .reviewContent(this.reviewContent)
                         .build();
    }
}