package MakeUs.Moira.controller.login;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.user.UserRole;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.advice.exception.EmailSignInFailedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Login"})
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final UserRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;


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
}


