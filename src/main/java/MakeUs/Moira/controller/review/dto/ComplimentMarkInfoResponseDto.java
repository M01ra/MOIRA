package MakeUs.Moira.controller.review.dto;

import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ComplimentMarkInfoResponseDto {
    private Long   complimentMarkId;
    private String complimentMarkName;
    private String complimentMarkContent;

    public ComplimentMarkInfoResponseDto(ComplimentMarkInfo complimentMarkInfo)
    {
        this.complimentMarkId = complimentMarkInfo.getId();
        this.complimentMarkName = complimentMarkInfo.getMarkName();
        this.complimentMarkContent = complimentMarkInfo.getContent();
    }
}
