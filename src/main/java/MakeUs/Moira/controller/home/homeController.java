package MakeUs.Moira.controller.home;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.fcm.dto.FcmTokenRequestDto;
import MakeUs.Moira.controller.home.dto.HomeResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.home.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"1.홈"})
@RequiredArgsConstructor
@RestController
public class homeController {

    private final HomeService     homeService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "홈화면",
            notes = "### 읽지 않은 알림과 메시지가 있는지 확인합니다.\n"
    )
    @GetMapping("/home")
    public SingleResult<HomeResponseDto> getHome(@RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        HomeResponseDto homeResponseDto = homeService.getHome(userId);
        return responseService.mappingSingleResult(homeResponseDto, "홈화면");
    }

}
