package MakeUs.Moira.controller.userPortfolio.userAward;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardResponseDto;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.service.userPortfolio.UserAwardService;
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
public class UserAwardController {

    private final UserAwardService userAwardService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService  responseService;
    private final Logger           logger = LoggerFactory.getLogger(this.getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 수상 추가 하기 팝업 - 등록하기",
            notes = "### 마이페이지 - 내 정보 수정하기 - 수상 추가 하기 팝업 - \"등록하기\"에 적용됩니다.\n"
    )
    @PostMapping(value = "/mypage/edit/award")
    public ListResult<UserAwardResponseDto> addUserAward(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                         @Valid @RequestBody UserAwardAddRequestDto userAwardAddRequestDto)
    {
        logger.info(userAwardAddRequestDto.toString());
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<UserAwardResponseDto> userAwardResponseDtoList = userAwardService.addUserAward(userId, userAwardAddRequestDto);
        logger.info(userAwardResponseDtoList.toString());
        return responseService.mappingListResult(userAwardResponseDtoList, "마이페이지 - 내 정보 수정하기 - 수상 정보 추가");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 수상 삭제",
            notes = "### 마이페이지 - 내 정보 수정하기 - 수상 내역을 삭제합니다.\n"
    )
    @DeleteMapping(value = "/mypage/edit/award/{userAwardId}")
    public CommonResult deleteUserAward(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                        @PathVariable Long userAwardId)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        userAwardService.deleteUserAward(userId, userAwardId);
        return responseService.mappingSuccessCommonResultOnly("마이페이지 - 내 정보 수정하기 - 수상 삭제");
    }
}
