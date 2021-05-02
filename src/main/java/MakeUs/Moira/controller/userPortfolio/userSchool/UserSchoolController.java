package MakeUs.Moira.controller.userPortfolio.userSchool;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolResponseDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.MajorInfoResponseDto;
import MakeUs.Moira.controller.userPortfolio.userSchool.dto.SchoolInfoResponseDto;
import MakeUs.Moira.service.userPortfolio.UserSchoolService;
import MakeUs.Moira.util.response.ResponseService;
import MakeUs.Moira.util.response.model.CommonResult;
import MakeUs.Moira.util.response.model.ListResult;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = {"6-3.마이페이지-프로필 수정-선택정보"})
@RequiredArgsConstructor
@RestController
public class UserSchoolController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserSchoolService userSchoolService;
    private final ResponseService responseService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "학교 검색",
            notes = "- 마이페이지 - 내 정보 수정하기 - 학력 추가하기 팝업 - 학교 추가하기 팝업\n" +
                    "- 학교를 검색할 때 적용됩니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage/edit/school-info")
    public ListResult<SchoolInfoResponseDto> searchSchoolInfo(
            @NotNull(message = "keyword에 빈 값을 넣을 수 없음") @ApiParam(value = "검색어", example = "서울대", required = true) @RequestParam String keyword) {
        // 권한 설정 : 유저만 접근
        List<SchoolInfoResponseDto> userSchoolResponseDtoList = userSchoolService.searchSchoolInfo(keyword);
        return responseService.mappingListResult(userSchoolResponseDtoList, "학교 검색 성공");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "학과 검색",
            notes = "- 마이페이지 - 내 정보 수정하기 - 학력 추가하기 팝업 - 전공 추가하기 팝업\n" +
                    "- 학과를 검색할 때 적용됩니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage/edit/major-info")
    public ListResult<MajorInfoResponseDto> searchMajorInfo(
            @NotNull(message = "keyword에 빈 값을 넣을 수 없음") @ApiParam(value = "검색어", example = "국어국문", required = true) @RequestParam String keyword) {
        // 권한 설정은 : 유저만 접근
        List<MajorInfoResponseDto> majorInfoResponseDtoList = userSchoolService.searchMajorInfo(keyword);
        return responseService.mappingListResult(majorInfoResponseDtoList, "학과 검색 성공");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 학력 추가 하기 팝업 - 등록하기",
            notes = "- 마이페이지 - 내 정보 수정하기 - 학력 추가 하기 팝업 - \"등록하기\"에 적용됩니다.\n" +
                    "- 등록하기를 누르지 않아도 적용됩니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/mypage/edit/school")
    public ListResult<UserSchoolResponseDto> addUserSchool(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @Valid @RequestBody UserSchoolAddRequestDto userSchoolAddRequestDto) {
        logger.info(userSchoolAddRequestDto.toString());
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<UserSchoolResponseDto> userSchoolResponseDtoList = userSchoolService.addUserSchool(userId, userSchoolAddRequestDto);
        logger.info(userSchoolResponseDtoList.toString());
        return responseService.mappingListResult(userSchoolResponseDtoList, "마이페이지 - 내 정보 수정하기 - 학력 정보 추가");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 학력 삭제",
            notes = "### 마이페이지 - 내 정보 수정하기 - 학력 내역을 삭제합니다.\n"
    )
    @DeleteMapping(value = "/mypage/edit/school/{userSchoolId}")
    public CommonResult deleteUserSchool(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                         @PathVariable Long userSchoolId)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        userSchoolService.deleteUserSchool(userId, userSchoolId);
        return responseService.mappingSuccessCommonResultOnly("마이페이지 - 내 정보 수정하기 - 학력 삭제");
    }
}
