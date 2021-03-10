package MakeUs.Moira.controller.user;

import MakeUs.Moira.advice.exception.UserException;
import MakeUs.Moira.controller.user.dto.UserSearchRequestDto;
import MakeUs.Moira.controller.user.dto.UserSaveRequestDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.user.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
//@RestController
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = false, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(msrl)로 회원을 조회한다")
    @GetMapping(value = "/user")
    public SingleResult<UserSearchRequestDto> findUserById() {

        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.mappingSingleResult(userService.findById(Long.valueOf(id)), "성공");
    }

    @PostMapping(value = "/user")
    public SingleResult<Long> save(@RequestBody UserSaveRequestDto userSaveRequestDto) throws UserException {
//        throw new UserException("UserException");
        Long savedId = userService.save(userSaveRequestDto);
        return responseService.mappingSingleResult(savedId, "사용자 저장 성공");
    }

}






