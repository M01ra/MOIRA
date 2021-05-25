package MakeUs.Moira.controller.userPortfolio.userLicense;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseResponseDto;
import MakeUs.Moira.service.userPortfolio.UserLicenseService;
import MakeUs.Moira.util.response.ResponseService;
import MakeUs.Moira.util.response.model.CommonResult;
import MakeUs.Moira.util.response.model.ListResult;
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

@Api(tags = {"8-3.마이페이지-프로필 수정-선택정보"})
@RequiredArgsConstructor
@RestController
public class UserLicenseController {

    private final UserLicenseService userLicenseService;
    private final JwtTokenProvider   jwtTokenProvider;
    private final ResponseService responseService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 자격증 추가 하기 팝업 - 등록하기",
            notes = "- 마이페이지 - 내 정보 수정하기 - 자격증 추가 하기 팝업 - \"등록하기\"에 적용됩니다.\n" +
                    "- 등록하기를 누르지 않아도 적용됩니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/users/license")
    public ListResult<UserLicenseResponseDto> addUserLicense(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @Valid @RequestBody UserLicenseAddRequestDto userLicenseAddRequestDto ) {
        logger.info(userLicenseAddRequestDto.toString());
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<UserLicenseResponseDto> userLicenseResponseDtoList = userLicenseService.addUserLicense(userId, userLicenseAddRequestDto);
        logger.info(userLicenseResponseDtoList.toString());
        return responseService.mappingListResult(userLicenseResponseDtoList, "마이페이지 - 내 정보 수정하기 - 자격증 정보 추가");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 자격증 삭제",
            notes = "### 마이페이지 - 내 정보 수정하기 - 자격증 내역을 삭제합니다.\n"
    )
    @DeleteMapping(value = "/users/license/{userLicenseId}")
    public CommonResult deleteUserLicense(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                          @PathVariable Long userLicenseId)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        userLicenseService.deleteUserLicense(userId, userLicenseId);
        return responseService.mappingSuccessCommonResultOnly("마이페이지 - 내 정보 수정하기 - 자격증 삭제");
    }
}
