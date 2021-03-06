package MakeUs.Moira.controller.userSecurity;

import MakeUs.Moira.domain.userSecurity.UserSecurity;
import MakeUs.Moira.domain.userSecurity.UserSecurityRepository;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.SingleResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"1. UserSecurityController"})
@RequiredArgsConstructor
@RequestMapping("/security")
@RestController
public class userSecurityController {

    private final UserSecurityRepository userSecurityRepository;
    private final ResponseService responseService;

    @ApiOperation(value = "회원 가입 정보 등록", notes = "회원 가입 정보를 등록합니다.")
    @PostMapping("")
    public SingleResult<UserSecurity> securityRegister(@ApiParam(value = "회원정보 제공사", required = true) @RequestParam String provider) {

        UserSecurity userSecurity = UserSecurity.builder()
                .provider(provider)
                .build();
        UserSecurity savedUserSecurity = userSecurityRepository.save(userSecurity);
        return responseService.mappingSingleResult(savedUserSecurity, "유저 등록 성공");
    }
}
