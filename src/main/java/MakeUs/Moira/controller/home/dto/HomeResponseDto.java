package MakeUs.Moira.controller.home.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HomeResponseDto {
    private boolean hasUnreadMessage;
    private boolean hasUnreadAlarm;

    @Builder
    public HomeResponseDto(boolean hasUnreadMessage, boolean hasUnreadAlarm) {
        this.hasUnreadMessage = hasUnreadMessage;
        this.hasUnreadAlarm = hasUnreadAlarm;
    }
}
