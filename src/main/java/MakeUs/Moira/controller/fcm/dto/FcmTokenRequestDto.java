package MakeUs.Moira.controller.fcm.dto;

import MakeUs.Moira.domain.user.UserFcmInfo;
import MakeUs.Moira.domain.user.User;
import lombok.Getter;

@Getter
public class FcmTokenRequestDto {
    private String fcmToken;

    public UserFcmInfo toEntity(User user) {
        return UserFcmInfo.builder()
                          .user(user)
                          .fcmToken(fcmToken)
                          .build();
    }
}
