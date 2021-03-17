package MakeUs.Moira.controller.security;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.user.dto.LoginRequestDto;
import MakeUs.Moira.domain.user.UserRole;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.security.LoginService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"로그인"})
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginService loginService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;


    @ApiOperation(
            value = "Jwt토큰 발급",
            notes = "Post의 Body로 socialProvider, AccessToken 값을 받은 후에 Jwt 토큰을 리턴\n"
                    + "socialProvider는 kakao, apple 를 입력받습니다."
    )
    @PostMapping(value = "/login")
    public SingleResult<String> getToken(@ApiParam(required = true) @RequestBody LoginRequestDto loginRequestDto) {

        String provider = loginRequestDto.getSocialProvider();
        String token = loginRequestDto.getAccessToken();

        String socialId = loginService.getUserSocialId(provider, token);
        Long userPk = loginService.findUserPkBySocialIdAndSocialProvider(socialId, provider);

        if (userPk == -1L){ // 신규 가입이면 -> 회원 등록시킨 후
            userPk = loginService.save(socialId, provider);
        }
        // jwt 발급
        String newJwtToken = jwtTokenProvider.createToken(String.valueOf(userPk), UserRole.USER.name());

        return responseService.mappingSingleResult(newJwtToken, "Jwt 토큰 발급");
    }
}


