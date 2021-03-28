package MakeUs.Moira.controller.fcm;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.fcm.dto.FcmTokenRequestDto;
import MakeUs.Moira.fcm.FcmService;
import MakeUs.Moira.fcm.model.alarmType;
import MakeUs.Moira.fcm.model.PushNotificationRequest;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.service.fcm.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Api(tags = {"1.FCM Test"})
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final ResponseService     responseService;
    private final JwtTokenProvider    jwtTokenProvider;
    private final FcmService          fcmService;


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "FCM Token 수신",
            notes = "### FCM Token을 받아 저장합니다.\n"
    )
    @PostMapping("/register")
    public CommonResult register(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                 @RequestBody FcmTokenRequestDto fcmTokenRequestDto)
    {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        notificationService.register(userId, fcmTokenRequestDto);
        return responseService.mappingSuccessCommonResultOnly("fcm 토큰 등록 성공");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "푸시알림 테스트",
            notes = "### FCM Token을 받아 저장합니다.\n"
    )
    @PostMapping("/push")
    public CommonResult pushTest(@RequestHeader(value = "X-AUTH-TOKEN") String token) throws ExecutionException, InterruptedException
    {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        fcmService.send(PushNotificationRequest.builder()
                                               .targetUserId(userId)
                                               .title(alarmType.MESSAGE_RECEIVED.name())
                                               .message(alarmType.MESSAGE_RECEIVED.name())
                                               .build());
        return responseService.mappingSuccessCommonResultOnly("fcm 토큰 등록 성공");
    }
}
