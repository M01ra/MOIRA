package MakeUs.Moira.domain.complimentMark;

import MakeUs.Moira.controller.userPool.dto.ComplimentMarkWithCountDto;
import MakeUs.Moira.domain.userReview.UserReview;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class ComplimentMarkInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String markName;

    private String content;

    public ComplimentMarkWithCountDto getComplimentMarkWithCountDto(List<UserReview> userReviewList){
        Long complimentMarkCount = userReviewList.stream()
                                                 .filter(userReview -> userReview.hasComplimentMark(this.id))
                                                 .count();
       return new ComplimentMarkWithCountDto(this, complimentMarkCount);
    }
}
