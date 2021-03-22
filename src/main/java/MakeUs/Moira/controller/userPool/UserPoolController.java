package MakeUs.Moira.controller.userPool;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.userPool.dto.UserPoolDetailProfileResponseDto;
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


@Api(tags = {"인재 풀"})
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
            value = "팀원 찾기 - 인재풀 - 등록",
            notes = "- 팀원 찾기 - 인재풀을 등록합니다.\n" +
                    "- 마이페이지에서 등록했던 프로필이 인재풀에 등록됩니다. \n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/pool")
    public CommonResult addUserPool(@RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        if (userPoolService.addUserPool(userId)) {
            return responseService.mappingSuccessCommonResultOnly("팀원 찾기 - 인재풀 - 등록 성공");
        }
        return responseService.mappingFailCommonResultOnly(-100, "팀원 찾기 - 인재풀 - 등록 실패");
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
        List<UserPoolResponseDto> userPoolResponseDtoList = userPoolService.getUserPoolList(userId, page, positionCategory, sortby);
        return responseService.mappingListResult(userPoolResponseDtoList, "팀원 찾기 - 인재풀");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 찾기 - 인재풀 - 게시글 좋아요 및 취소\n",
            notes = "- 팀원 찾기 - 인재풀의 게시글에 좋아요를 등록/취소합니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/pool/like")
    public SingleResult<UserPoolLikeAddResponse> addUserPoolLike(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                 @ApiParam(value = "인재풀 게시글 id", required = true) @RequestParam Long userPoolId)
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
            value = "팀원 찾기 - 인재풀 - 게시글 상세 - 사용자 정보\n",
            notes = "- 팀원 찾기 - 인재풀의 게시글을 클릭해서 상세 정보 - 사용자 정보를 조회합니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/pool/profile")
    public SingleResult<UserPoolDetailProfileResponseDto> getUserPoolDetailProfile(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                                   @ApiParam(value = "인재풀 게시글 id", required = true) @RequestParam Long userPoolId)
    {
        // 권한 설정은 시큐리티에서 하자
        UserPoolDetailProfileResponseDto userPoolDetailProfileResponseDto = userPoolService.getUserPoolDetailProfile(userPoolId);
        return responseService.mappingSingleResult(userPoolDetailProfileResponseDto, "팀원 찾기 - 인재풀 - 게시글 상세 - 사용자 정보");
    }
}
