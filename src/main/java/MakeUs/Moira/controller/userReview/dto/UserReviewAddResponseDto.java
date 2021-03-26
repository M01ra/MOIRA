package MakeUs.Moira.controller.userReview.dto;

import MakeUs.Moira.domain.userReview.UserReview;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class UserReviewAddResponseDto {

    private Long                    userProjectId;
    private int                     mannerPoint;
    private String                  reviewContent;
    private List<ComplimentMarkDto> complimentMarkInfoList;

    public UserReviewAddResponseDto(UserReview userReview)
    {
        this.userProjectId = userReview.getUserProject()
                                       .getId();
        this.mannerPoint = userReview.getMannerPoint();
        this.reviewContent = userReview.getReviewContent();
        this.complimentMarkInfoList = userReview.getUserReviewComplimentMarkList()
                                                .stream()
                                                .map(ComplimentMarkDto::new)
                                                .collect(Collectors.toList());
    }
}
