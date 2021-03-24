package MakeUs.Moira.controller.userPool.dto;

import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;
import MakeUs.Moira.domain.userReview.UserReviewComplimentMark;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ComplimentMarkCountDto {
    private Long complimentMarkId;
    private String complimentMarkName;
    private String complimentMarkContent;
    private Long complimentMarkCount = 0L;


    public ComplimentMarkCountDto(ComplimentMarkInfo complimentMarkInfo)
    {
        this.complimentMarkId = complimentMarkInfo.getId();
        this.complimentMarkName = complimentMarkInfo.getMarkName();
        this.complimentMarkContent = complimentMarkInfo.getContent();
    }

    public void updateComplimentMarkCount(Long count){
        this.complimentMarkCount = count;
    }

}
