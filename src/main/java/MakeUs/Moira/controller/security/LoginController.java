package MakeUs.Moira.controller.security;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.domain.user.UserRole;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.security.TokenService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"로그인"})
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final TokenService tokenService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

//    @ApiImplicitParams({@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 JWT_TOKEN", required = true, dataType = "String", paramType = "header")})
    @ApiOperation(
            value = "Jwt토큰 발급",
            notes = "파라미터로 provider, AccessToken 값을 받은 후에 Jwt 토큰을 리턴"
    )
    @PostMapping(value = "/login")
    public SingleResult<String> getToken(@ApiParam(value = "AccessToken", required = true) @RequestParam String token, //
                                         @ApiParam(value = "kakao or apple", required = true) @RequestParam String provider) {
        String socialId = tokenService.getUserSocialId(provider, token);
        Long userPk = tokenService.findBySocialIdAndSocialProvider(socialId, provider).getUserPk();

        if (userPk == -1L){ // 신규 가입이면 -> 회원 등록시킨 후
            userPk = tokenService.save(socialId, provider);
        }
        // jwt 발급
        String newJwtToken = jwtTokenProvider.createToken(String.valueOf(userPk), UserRole.USER.name());

        return responseService.mappingSingleResult(newJwtToken, "Jwt 토큰 발급");
    }

    /*
    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email) {

        // 이메일로 찾고 없으면 예외 발생 시킴
        User user = userJpaRepo.findByEmail(email).orElseThrow(EmailSignInFailedException::new);

        String token = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getUserRole().name());
        return responseService.mappingSingleResult(token, "Jwt 토큰 발급");
    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String email,
                               @ApiParam(value = "이름", required = true) @RequestParam String nickname) {

        userJpaRepo.save(User.builder()
                .email(email)
                .nickname(nickname)
                .userRole(UserRole.USER)
                .build());
        return responseService.mappingSuccessCommonResultOnly("성공");
    }
    */
}


