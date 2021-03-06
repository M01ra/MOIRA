package MakeUs.Moira.controller.security;

import MakeUs.Moira.controller.security.dto.LoginRequestDto;
import MakeUs.Moira.controller.security.dto.LoginResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.security.LoginService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = {"1. 로그인"})
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService    loginService;
    private final ResponseService responseService;
    private final Logger          logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(
            value = "Jwt토큰 발급",
            notes = "### Post의 Body로 socialProvider, AccessToken 값을 받은 후에 Jwt 토큰을 리턴\n" +
                    "### socialProvider는 kakao를 입력받습니다."
    )
    @PostMapping(value = "/login")
    public SingleResult<LoginResponseDto> getToken(@Valid @ApiParam(required = true) @RequestBody LoginRequestDto loginRequestDto) {

        logger.info(loginRequestDto.toString());
        LoginResponseDto loginResponseDto = loginService.getToken(loginRequestDto);
        return responseService.mappingSingleResult(loginResponseDto, "Jwt토큰 발급");
    }
}


