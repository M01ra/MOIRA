package MakeUs.Moira.fcm.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PushNotificationRequest {
    private Long   targetUserId;
    private String title;
    private String message;

    @Builder
    public PushNotificationRequest(Long targetUserId, String title, String message) {
        this.targetUserId = targetUserId;
        this.title = title;
        this.message = message;
    }
}
