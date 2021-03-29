package MakeUs.Moira.controller.home.dto;

import MakeUs.Moira.domain.alarm.AlarmType;
import MakeUs.Moira.domain.chat.ReadStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AlarmResponseDto {

    @ApiModelProperty(value = "알람 타입", example = "APPLY")
    private AlarmType alarmType;
    @ApiModelProperty(value = "알람 ID", example = "1")
    private Long    alarmTargetId;
    @ApiModelProperty(value = "알람 내용", example = "모집글 제목 모집...에 새 댓글이 달렸습니다.")
    private String  alarmContent;
    @ApiModelProperty(value = "알람 읽었는지 여부", example = "true")
    private boolean isRead;

    @Builder
    public AlarmResponseDto(AlarmType alarmType,
                            Long alarmTargetId,
                            String alarmContent,
                            ReadStatus readStatus)
    {
        this.alarmType = alarmType;
        this.alarmTargetId = alarmTargetId;
        this.alarmContent = alarmContent;
        this.isRead = enumToBoolean(readStatus);
    }

    private boolean enumToBoolean(ReadStatus readStatus){
        return readStatus.equals(ReadStatus.READ);
    }
}
