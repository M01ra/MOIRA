package MakeUs.Moira.controller.complimentMark.dto;

import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;
import lombok.Getter;

@Getter
public class ComplimentMarkInfoResponseDto {
    private Long   complimentMarkId;
    private String complimentMarkImageUrl;
    private String complimentMarkName;
    private String complimentMarkContent;

    public ComplimentMarkInfoResponseDto(ComplimentMarkInfo complimentMarkInfo)
    {
        this.complimentMarkId = complimentMarkInfo.getId();
        this.complimentMarkImageUrl = complimentMarkInfo.getComplimentMarkImageUrl();
        this.complimentMarkName = complimentMarkInfo.getMarkName();
        this.complimentMarkContent = complimentMarkInfo.getContent();
    }
}
