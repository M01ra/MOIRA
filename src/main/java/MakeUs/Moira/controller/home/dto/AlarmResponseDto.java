package MakeUs.Moira.controller.home.dto;

import MakeUs.Moira.domain.chat.ReadStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AlarmResponseDto {

    private Long    alarmId;
    private String  alarmType;
    private Long    alarmTargetId;
    private String  alarmContent;
    private boolean isRead;

    @Builder
    public AlarmResponseDto(Long alarmId,
                            String alarmType,
                            Long alarmTargetId,
                            String alarmContent,
                            ReadStatus readStatus)
    {
        this.alarmId = alarmId;
        this.alarmType = alarmType;
        this.alarmTargetId = alarmTargetId;
        this.alarmContent = alarmContent;
        this.isRead = enumToBoolean(readStatus);
    }

    private boolean enumToBoolean(ReadStatus readStatus){
        return readStatus.equals(ReadStatus.READ);
    }
}
