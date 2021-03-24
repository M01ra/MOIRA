package MakeUs.Moira.controller.userPool;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.userPool.dto.UserPoolDetailProfileResponseDto;
import MakeUs.Moira.controller.userPool.dto.UserPoolDetailReviewResponseDto;
import MakeUs.Moira.controller.userPool.dto.UserPoolLikeAddResponse;
import MakeUs.Moira.controller.userPool.dto.UserPoolResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.userPool.UserPoolService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = {"6.인재 풀"})
@RequiredArgsConstructor
@RestController
public class UserPoolController {

    private final UserPoolService  userPoolService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService  responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 찾기 - 인재풀 - ON/OFF",
            notes = "- 팀원 찾기 - 인재풀을 ON/OFF 합니다.\n" +
                    "- 마이페이지에서 등록했던 프로필이 인재풀에 등록됩니다. \n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/pool")
    public CommonResult switchUserPoolVisibility(@RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        userPoolService.switchUserPoolVisibility(userId);
        return responseService.mappingSuccessCommonResultOnly("팀원 찾기 - 인재풀 - 등록 성공");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 찾기 - 인재풀",
            notes = "- 팀원 찾기 - 인재풀의 첫 화면입니다.\n" +
                    "- 페이징 처리가 되며, 한 페이지에 10개씩 가져옵니다. Ex) 1페이지 -> page=1\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/pool")
    public ListResult<UserPoolResponseDto> getUserPoolList(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @ApiParam(value = "포지션 카테고리 필터", required = true, allowableValues = "develop, director, designer") @RequestParam String positionCategory,
            @ApiParam(value = "정렬 방식 필터", required = true, allowableValues = "date, hit, like") @RequestParam String sortby,
            @ApiParam(value = "현재 페이지 위치", required = true) @RequestParam int page)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<UserPoolResponseDto> userPoolResponseDtoList = userPoolService.getUserPool(userId, page, positionCategory, sortby);
        return responseService.mappingListResult(userPoolResponseDtoList, "팀원 찾기 - 인재풀");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 찾기 - 인재풀 - 검색",
            notes = "- 팀원 찾기 - 인재풀 - 검색\n" +
                    "- 닉네임(3글자 이상)으로 인재풀 게시물을 검색합니다. \n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/pool/search")
    public ListResult<UserPoolResponseDto> searchByNickname(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                            @ApiParam(value = "검색 키워드", required = true) @RequestParam String keyword)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<UserPoolResponseDto> userPoolSearchResponseDtoList = userPoolService.getUserPoolByNickname(userId, keyword);
        return responseService.mappingListResult(userPoolSearchResponseDtoList, "팀원 찾기 - 인재풀 - 검색");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 찾기 - 인재풀 - 게시글 좋아요 ON/OFF\n",
            notes = "- 팀원 찾기 - 인재풀의 게시글에 좋아요를 ON/OFF 합니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/pool/{userPoolId}/like")
    public SingleResult<UserPoolLikeAddResponse> addUserPoolLike(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                 @ApiParam(value = "인재풀 게시글 id", required = true) @PathVariable Long userPoolId)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        UserPoolLikeAddResponse UserPoolLikeAddResponse = userPoolService.updateUserPoolLike(userId, userPoolId);
        return responseService.mappingSingleResult(UserPoolLikeAddResponse, "팀원 찾기 - 인재풀 - 게시글 좋아요");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 찾기 - 인재풀 - 게시글 상세(사용자정보)\n",
            notes = "- 팀원 찾기 - 인재풀 - 게시글 상세를 조회합니다.\n" +
                    "## API를 호출할 때 마다 해당 인재풀 게시글의 조회 수가 1이 증가합니다."
    )
    @GetMapping(value = "/pool/{userPoolId}/profile")
    public SingleResult<UserPoolDetailProfileResponseDto> getUserPoolDetailProfile(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @ApiParam(value = "인재풀 게시글 id", required = true) @PathVariable Long userPoolId)
    {
        // 권한 설정은 시큐리티에서 하자
        UserPoolDetailProfileResponseDto userPoolDetailProfileResponseDto = userPoolService.getUserPoolDetailProfile(userPoolId);
        return responseService.mappingSingleResult(userPoolDetailProfileResponseDto, "팀원 찾기 - 인재풀 - 게시글 상세");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 찾기 - 인재풀 - 게시글 상세(사용자평가)\n",
            notes = "- 팀원 찾기 - 인재풀 - 게시글 상세를 조회합니다.\n"
    )
    @GetMapping(value = "/pool/{userPoolId}/review")
    public SingleResult<UserPoolDetailReviewResponseDto> getUserPoolDetailReview(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @ApiParam(value = "인재풀 게시글 id", required = true) @PathVariable Long userPoolId)
    {
        // 권한 설정은 시큐리티에서 하자
        UserPoolDetailReviewResponseDto userPoolDetailReviewResponseDto = userPoolService.getUserPoolDetailReview(userPoolId);
        return responseService.mappingSingleResult(userPoolDetailReviewResponseDto, "팀원 찾기 - 인재풀 - 게시글 상세");
    }
}
