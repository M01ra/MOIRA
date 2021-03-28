package MakeUs.Moira.controller.home.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HomeResponseDto {
    private boolean existUnreadMessage;
    private boolean existUnreadAlarm;

    @Builder
    public HomeResponseDto(boolean existUnreadMessage, boolean existUnreadAlarm) {
        this.existUnreadMessage = existUnreadMessage;
        this.existUnreadAlarm = existUnreadAlarm;
    }
}
