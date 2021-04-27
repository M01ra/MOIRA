package MakeUs.Moira.controller.review.dto;

import MakeUs.Moira.controller.userPool.dto.ComplimentMarkWithCountDto;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.userReview.UserReview;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class UserReviewResponseDto {
    private String                           nickname;
    private Double                           avgMannerPoint;
    private String                           recentReviewContent;
    private List<ComplimentMarkWithCountDto> complimentMarkWithCountDtoList;
    private Long                             maxComplimentMarkId;

    public UserReviewResponseDto(String nickname,
                                 List<UserReview> userReviewList,
                                 List<ComplimentMarkWithCountDto> complimentMarkWithCountDtoList)
    {
        this.nickname = nickname;
        this.avgMannerPoint = userReviewList.stream()
                                            .map(UserReview::getMannerPoint)
                                            .collect(Collectors.averagingDouble(mannerPoint -> (double) mannerPoint));
        this.recentReviewContent = getFirstReviewContent(userReviewList);
        this.complimentMarkWithCountDtoList = complimentMarkWithCountDtoList;
        this.maxComplimentMarkId = complimentMarkWithCountDtoList.stream()
                                                                 .max(Comparator.comparing(ComplimentMarkWithCountDto::getComplimentMarkCount))
                                                                 .get()
                                                                 .getComplimentMarkId();
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
