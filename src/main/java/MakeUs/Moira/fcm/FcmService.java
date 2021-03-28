package MakeUs.Moira.fcm;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.domain.alarm.AlarmHistory;
import MakeUs.Moira.domain.alarm.AlarmHistoryRepo;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserFcmInfo;
import MakeUs.Moira.domain.user.UserFcmInfoRepo;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.fcm.model.PushNotificationRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class FcmService {

    private final Logger           logger = LoggerFactory.getLogger(this.getClass());
    private final UserFcmInfoRepo  userFcmInfoRepo;
    private final AlarmHistoryRepo alarmHistoryRepo;

    @Transactional
    public void send(final PushNotificationRequest pushNotificationRequest) throws ExecutionException, InterruptedException {

        UserFcmInfo userFcmInfo = userFcmInfoRepo.findByUser_Id(pushNotificationRequest.getTargetUserId())
                                                 .orElseThrow(() -> new CustomException(ErrorCode.NON_EXIST_FCM_TOKEN));
        Message message = Message.builder()
                                 .setToken(userFcmInfo.getFcmToken())
                                 .setWebpushConfig(WebpushConfig.builder()
                                                                .putHeader("ttl", "300")
                                                                .setNotification(new WebpushNotification(pushNotificationRequest.getTitle(), pushNotificationRequest.getMessage()))
                                                                .build())
                                 .build();
        String response = FirebaseMessaging.getInstance()
                                           .sendAsync(message)
                                           .get();
        logger.info("Sent Message: " + response);
        alarmHistoryRepo.save(AlarmHistory.builder()
                                          .userId(pushNotificationRequest.getTargetUserId())
                                          .alarmContent(pushNotificationRequest.getMessage())
                                          .build());
    }
}
