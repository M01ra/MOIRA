package MakeUs.Moira.controller.user;


import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.service.user.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"회원가입"})
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @ApiOperation(
            value = "닉네임 중복 검사",
            notes = "닉네임을 전달받아서 중복이 아니면\n"
                    +"성공 : 200\n"
                    +"실패: -100")
    @PostMapping(value = "/signup/nickname")
    public CommonResult isDuplicatedNickname(@ApiParam(value = "nickname", required = true) @RequestParam String nickname) {
        if (userService.isDuplicatedNickname(nickname))
            return responseService.mappingFailCommonResultOnly(-100, "중복된 닉네임입니다");
        else
            return responseService.mappingSuccessCommonResultOnly("사용가능한 닉네임입니다");
    }
}






