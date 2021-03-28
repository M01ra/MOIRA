package MakeUs.Moira.controller.home;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.home.dto.HomeResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.controller.home.dto.AlarmResponseDto;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.home.HomeService;
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
public class homeController {

    private final HomeService      homeService;
    private final ResponseService  responseService;
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
            notes = "### 읽지 않은 알림과 메시지가 있는지 확인합니다.\n"
    )
    @GetMapping("/home")
    public SingleResult<HomeResponseDto> getHome(@RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        HomeResponseDto homeResponseDto = homeService.getHome(userId);
        logger.info(homeResponseDto.toString());
        return responseService.mappingSingleResult(homeResponseDto, "홈화면");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "홈화면 - 알람 목록",
            notes = "### 홈화면에서 알람 목록을 확인합니다.\n" +
                    "### 페이징이 적용되며 10개씩 목록을 가져옵니다."
    )
    @GetMapping("/home/alarm")
    public ListResult<AlarmResponseDto> getAlarm(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                 @ApiParam(value = "페이지 Ex) page=1", required = true) @RequestParam int page)
    {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<AlarmResponseDto> alarmResponseDtoList = homeService.getAlarm(userId, page);
        logger.info(alarmResponseDtoList.toString());
        return responseService.mappingListResult(alarmResponseDtoList, "홈화면 - 알람 목록");
    }

}
