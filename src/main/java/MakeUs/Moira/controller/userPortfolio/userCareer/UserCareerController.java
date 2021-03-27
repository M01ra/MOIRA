package MakeUs.Moira.controller.userPortfolio.userCareer;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerResponseDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.service.userPortfolio.UserCareerService;
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
public class UserCareerController {

    private final UserCareerService userCareerService;
    private final JwtTokenProvider  jwtTokenProvider;
    private final ResponseService   responseService;
    private final Logger            logger = LoggerFactory.getLogger(this.getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 경력 추가 하기 팝업 - 등록하기",
            notes = "- 마이페이지 - 내 정보 수정하기 - 경력 추가 하기 팝업 - \"등록하기\"에 적용됩니다.\n" +
                    "- 등록하기를 누르지 않아도 적용됩니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/mypage/edit/career")
    public ListResult<UserCareerResponseDto> addUserCareer(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @Valid @RequestBody UserCareerAddRequestDto userCareerAddRequestDto)
    {
        logger.info(userCareerAddRequestDto.toString());
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<UserCareerResponseDto> userCareerResponseDtoList = userCareerService.addUserCareer(userId, userCareerAddRequestDto);
        logger.info(userCareerResponseDtoList.toString());
        return responseService.mappingListResult(userCareerResponseDtoList, "마이페이지 - 내 정보 수정하기 - 경력 정보 추가");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 경력 삭제",
            notes = "### 마이페이지 - 내 정보 수정하기 - 경력 내역을 삭제합니다.\n"
    )
    @DeleteMapping(value = "/mypage/edit/career/{userCareerId}")
    public CommonResult deleteUserCareer(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                         @PathVariable Long userCareerId)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        userCareerService.deleteUserCareer(userId, userCareerId);
        return responseService.mappingSuccessCommonResultOnly("마이페이지 - 내 정보 수정하기 - 경력 삭제");
    }
}
