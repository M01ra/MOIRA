package MakeUs.Moira.controller.userPortfolio;


import MakeUs.Moira.controller.userPortfolio.dto.MajorInfoResponseDto;
import MakeUs.Moira.controller.userPortfolio.dto.SchoolInfoResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.service.userPortfolio.UserPortfolioService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"학교/학과 정보 검색"})
@RequiredArgsConstructor
@RestController
public class userPortfolioController {

    private final UserPortfolioService userPortfolioService;
    private final ResponseService      responseService;

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
    @GetMapping(value = "/mypage/school-info")
    public CommonResult searchSchoolInfo(@ApiParam(value = "검색어", example = "서울대", required = true) @RequestParam String keyword) {
        // 권한 설정 : 유저만 접근
        List<SchoolInfoResponseDto> userSchoolResponseDtoList = userPortfolioService.searchSchoolInfo(keyword);
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
    @GetMapping(value = "/mypage/major-info")
    public CommonResult searchMajorInfo(@ApiParam(value = "검색어", example = "국어국문", required = true) @RequestParam String keyword) {
        // 권한 설정은 : 유저만 접근
        List<MajorInfoResponseDto> majorInfoResponseDtoList = userPortfolioService.searchMajorInfo(keyword);
        return responseService.mappingListResult(majorInfoResponseDtoList, "학과 검색 성공");
    }
}
