package MakeUs.Moira.controller.userReview.dto;

import MakeUs.Moira.controller.userPool.dto.ComplimentMarkWithCountDto;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.userReview.UserReview;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Getter
public class UserReviewResponseDto {
    private Double                           avgMannerPoint;
    private String                           recentReviewContent;
    private List<ComplimentMarkWithCountDto> complimentMarkWithCountDtoList;

    public UserReviewResponseDto(List<UserReview> userReviewList,
                                 List<ComplimentMarkWithCountDto> complimentMarkWithCountDtoList)
    {
        
        this.avgMannerPoint = userReviewList.stream()
                                            .map(UserReview::getMannerPoint)
                                            .collect(Collectors.averagingDouble(mannerPoint -> (double) mannerPoint));
        this.recentReviewContent = getFirstReviewContent(userReviewList);
        this.complimentMarkWithCountDtoList = complimentMarkWithCountDtoList;
    }

    private String getFirstReviewContent(List<UserReview> userReviewList) {
        if (userReviewList.isEmpty()) {
            return "";
        }
        sortByMostRecent(userReviewList);
        return userReviewList.get(0)
                             .getReviewContent();
    }

    private void sortByMostRecent(List<UserReview> userReviewList) {
        userReviewList.sort(Comparator.comparing(AuditorEntity::getCreatedDate)
                                      .reversed());
    }
}