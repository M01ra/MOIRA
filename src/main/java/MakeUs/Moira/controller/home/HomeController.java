package MakeUs.Moira.controller.home;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.alarm.dto.AlarmReadStatusUpdateResponseDto;
import MakeUs.Moira.controller.home.dto.HomeResponseDto;
import MakeUs.Moira.controller.alarm.dto.AlarmResponseDto;
import MakeUs.Moira.service.home.HomeService;
import MakeUs.Moira.util.response.ResponseService;
import MakeUs.Moira.util.response.model.ListResult;
import MakeUs.Moira.util.response.model.SingleResult;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"1.홈"})
@RequiredArgsConstructor
@RestController
public class HomeController {

    private final HomeService      homeService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger           logger = LoggerFactory.getLogger(this.getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "홈화면",
            notes = "### 읽지 않은 알림과 메시지가 있는지 확인합니다.\n" +
                    "![image](https://moira-springboot.s3.ap-northeast-2.amazonaws.com/007e6c69-8e30-4f03-b088-7e4326a2e887.png)\n"
    )
    @GetMapping("/home")
    public SingleResult<HomeResponseDto> getHome(@RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        HomeResponseDto homeResponseDto = homeService.getHome(userId);
        logger.info(homeResponseDto.toString());
        return responseService.mappingSingleResult(homeResponseDto, "홈화면");
    }

}
