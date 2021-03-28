package MakeUs.Moira.service.fcm;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.fcm.dto.FcmTokenRequestDto;
import MakeUs.Moira.domain.user.UserFcmInfo;
import MakeUs.Moira.domain.user.UserFcmInfoRepo;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final UserRepo        userRepo;
    private final UserFcmInfoRepo userFcmInfoRepo;

    @Transactional
    public void register(Long userId, FcmTokenRequestDto fcmTokenRequestDto) {
        userFcmInfoRepo.findByUser_Id(userId)
                       .map(userFcmInfo -> {
                           userFcmInfo.updateFcmToken(fcmTokenRequestDto.getFcmToken());
                           return userFcmInfo;
                       })
                       .orElseGet(() -> {
                           User userEntity = userRepo.findById(userId)
                                                     .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
                           UserFcmInfo userFcmInfo = fcmTokenRequestDto.toEntity(userEntity);
                           return userFcmInfoRepo.save(userFcmInfo);
                       });
    }
}
