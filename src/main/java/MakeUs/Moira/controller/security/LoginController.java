package MakeUs.Moira.controller.security;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.domain.user.UserRole;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.security.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "Jwt토큰 발급", notes = "AccessToken을 전달받아, Jwt 토큰 발급")
    @PostMapping(value = "/signup")
    public SingleResult<String> getToken(@ApiParam(value = "AccessToken", required = true) @RequestParam String token, //
                                         @ApiParam(value = "kakao or apple", required = true) @RequestParam String provider) {
        /*
        1. AccessToken을 꺼냄
        2. AccessToken이 해당 Provider에서 온 게 맞는지 확인 -> 인터페이스로 처리
            a) 아니면 예외 발생시킴
        3. TokenService 에서 AccessToken 으로 사용자 id 받아옴
        4. 이미 가입된 사용자인지확인
            a) 있으면(기존회원) -> jwt발급
            b) 없으면(신규회원)-> 회원가입시켜줌
         */

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


