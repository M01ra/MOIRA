package MakeUs.Moira.controller.user;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.user.dto.MyPageResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.service.user.MyPageService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"마이페이지"})
@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지",
            notes = "하단 네비게이션 바를 눌렀을 때, 마이페이지의 첫 화면입니다.\n" +
                    "비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage")
    public CommonResult signup(@RequestHeader(value = "X-AUTH-TOKEN", required = true) String token) {
//        if (token == null) throw new CustomAuthorizationException("로그인이 필요한 기능");
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        MyPageResponseDto myPageResponseDto = myPageService.getMyPage(userId);

        return responseService.mappingSingleResult(myPageResponseDto, "마이페이지 로드 성공");
    }
}
