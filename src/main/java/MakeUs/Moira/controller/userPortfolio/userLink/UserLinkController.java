package MakeUs.Moira.controller.userPortfolio.userLink;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.userPortfolio.userLink.dto.UserLinkAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userLink.dto.UserLinkResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.service.userPortfolio.UserLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"6-3.마이페이지-프로필 수정-선택정보"})
@RequiredArgsConstructor
@RestController
public class UserLinkController {

    private final UserLinkService  userLinkService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService  responseService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 링크 추가 하기 팝업 - 등록하기",
            notes = "- 마이페이지 - 내 정보 수정하기 - 링크 추가 하기 팝업 - \"등록하기\"에 적용됩니다.\n" +
                    "- 등록하기를 누르지 않아도 적용됩니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/mypage/edit/link")
    public ListResult<UserLinkResponseDto> addUserLink(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @Valid @RequestBody UserLinkAddRequestDto userLinkAddRequestDto ) {
        logger.info(userLinkAddRequestDto.toString());
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<UserLinkResponseDto> userLinkResponseDtoList = userLinkService.addUserLink(userId, userLinkAddRequestDto);
        logger.info(userLinkResponseDtoList.toString());
        return responseService.mappingListResult(userLinkResponseDtoList, "마이페이지 - 내 정보 수정하기 - 링크 정보 추가");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 링크 삭제",
            notes = "### 마이페이지 - 내 정보 수정하기 - 링크 내역을 삭제합니다.\n"
    )
    @DeleteMapping(value = "/mypage/edit/link/{userLinkId}")
    public CommonResult deleteUserLink(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                        @PathVariable Long userLinkId)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        userLinkService.deleteUserLink(userId, userLinkId);
        return responseService.mappingSuccessCommonResultOnly("마이페이지 - 내 정보 수정하기 - 링크 삭제");
    }
}
