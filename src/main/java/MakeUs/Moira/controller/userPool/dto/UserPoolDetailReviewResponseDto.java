package MakeUs.Moira.controller.userPool.dto;


import MakeUs.Moira.domain.userReview.UserReviewComplimentMark;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class UserPoolDetailReviewResponseDto {
    private Double                       avgMannerPoint;
    private String                       recentReviewContent;
    private List<ComplimentMarkCountDto> complimentMarkCountDtoList = new ArrayList<>();

    public UserPoolDetailReviewResponseDto(List<UserReviewComplimentMark> userReviewComplimentMarkList,
                                           List<ComplimentMarkCountDto> complimentMarkCountDtoList)
    {
//        this.avgMannerPoint = userReviewList.stream()
//                                            .collect(Collectors.averagingDouble(userReview -> (double) userReview.getMannerPoint()));
//        this.recentReviewContent = userReviewList.get(0)
//                                                 .getReviewContent();
        this.avgMannerPoint = userReviewComplimentMarkList.stream()
                                                          .map(userReviewComplimentMark -> userReviewComplimentMark.getUserReview()
                                                                                                                   .getMannerPoint())

                                                          .collect(Collectors.averagingDouble(mannerPoint -> mannerPoint));
        this.recentReviewContent = userReviewComplimentMarkList.get(0).getUserReview().getReviewContent();
        this.complimentMarkCountDtoList = complimentMarkCountDtoList;
    }
}
