package MakeUs.Moira.controller.userPool.dto;

import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;
import MakeUs.Moira.domain.userReview.UserReviewComplimentMark;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ComplimentMarkWithCountDto {
    private Long   complimentMarkId;
    private String complimentMarkName;
    private String complimentMarkContent;
    private Long   complimentMarkCount = 0L;


    public ComplimentMarkWithCountDto(ComplimentMarkInfo complimentMarkInfo, Long complimentMarkCount)
    {
        this.complimentMarkId = complimentMarkInfo.getId();
        this.complimentMarkName = complimentMarkInfo.getMarkName();
        this.complimentMarkContent = complimentMarkInfo.getContent();
        this.complimentMarkCount = complimentMarkCount;
    }
}
