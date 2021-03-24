package MakeUs.Moira.controller.user;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.user.dto.myPageEdit.MyPageEditResponseDto;
import MakeUs.Moira.controller.user.dto.myPageEdit.MyPageEditProfileUpdateResponseDto;
import MakeUs.Moira.controller.user.dto.myPageEdit.MyPageEditProfileUpdateRequestDto;

import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.user.MyPageEditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"6-2.마이페이지-프로필 수정"})
@RequiredArgsConstructor
@RestController
public class MyPageEditController {

    private final ResponseService   responseService;
    private final JwtTokenProvider  jwtTokenProvider;
    private final MyPageEditService myPageEditService;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 첫 화면",
            notes = "- 마이페이지 - 내 정보 수정하기의 첫 화면을 가져옵니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage/edit")
    public SingleResult<MyPageEditResponseDto> getMyPageEdit(@RequestHeader(value = "X-AUTH-TOKEN") String token) {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        MyPageEditResponseDto myPageEditResponseDto = myPageEditService.getMyPageEdit(userId);
        return responseService.mappingSingleResult(myPageEditResponseDto, "마이페이지 - 내 정보 수정하기 - 첫 화면");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 프로필 관련 정보 수정하기(프로필 사진 제외)",
            notes = "- 마이페이지 - 내 정보 수정하기의 프로필 관련 정보를 수정할 때 사용됩니다. 수정할 때 적용됩니다.\n" +
                    "- 등록하기를 누르지 않으면 적용되지 않습니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PutMapping(value = "/mypage/edit/profile")
    public SingleResult<MyPageEditProfileUpdateResponseDto> updateMyPageEditProfile(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @RequestBody MyPageEditProfileUpdateRequestDto myPageEditProfileUpdateRequestDto) {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        MyPageEditProfileUpdateResponseDto myPageEditProfileUpdateResponseDto = myPageEditService.updateMyPageProfile(userId, myPageEditProfileUpdateRequestDto);
        return responseService.mappingSingleResult(myPageEditProfileUpdateResponseDto, "마이페이지 - 내 정보 수정하기 - 프로필 관련 정보 수정하기(프로필 사진 제외) 성공");
    }
}
