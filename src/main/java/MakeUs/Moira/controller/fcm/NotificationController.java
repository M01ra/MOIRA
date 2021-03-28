package MakeUs.Moira.controller.fcm;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.fcm.dto.FcmTokenRequestDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.service.fcm.NotificationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final ResponseService     responseService;
    private final JwtTokenProvider    jwtTokenProvider;


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
}
