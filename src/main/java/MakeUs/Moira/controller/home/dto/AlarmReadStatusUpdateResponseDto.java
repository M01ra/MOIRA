package MakeUs.Moira.controller.home.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AlarmReadStatusUpdateResponseDto {
    private Long    alarmId;
    private boolean read;

    @Builder
    public AlarmReadStatusUpdateResponseDto(Long alarmId, boolean read) {
        this.alarmId = alarmId;
        this.read = read;
    }
}
