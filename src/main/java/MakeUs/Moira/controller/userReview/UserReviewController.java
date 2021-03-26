package MakeUs.Moira.controller.userReview;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.userReview.dto.UserReviewAddRequestDto;
import MakeUs.Moira.controller.userReview.dto.UserReviewAddResponseDto;
import MakeUs.Moira.controller.userReview.dto.UserReviewDetailResponseDto;
import MakeUs.Moira.controller.userReview.dto.UserReviewResponseDto;
import MakeUs.Moira.response.ResponseService;

import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.userReview.UserReviewService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;


@Api(tags = {"5.유저 리뷰"})
@RequiredArgsConstructor
@RestController
public class UserReviewController {

    private final UserReviewService userReviewService;
    private final ResponseService   responseService;

    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 평가하기 - 특정 유저 평가하기",
            notes = "### 팀원 평가하기 - 특정 유저 평가하기를 등록합니다.\n" +
                    "### 완료하기를 누르면 적용됩니다.\n" +
                    "### 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/review")
    public SingleResult<UserReviewAddResponseDto> addUserReview(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @Valid @RequestBody UserReviewAddRequestDto userReviewAddRequestDto)
    {
        logger.info(userReviewAddRequestDto.toString());
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        UserReviewAddResponseDto userReviewAddResponseDto = userReviewService.addUserReview(userId, userReviewAddRequestDto);
        logger.info(userReviewAddResponseDto.toString());
        return responseService.mappingSingleResult(userReviewAddResponseDto, "팀원 평가하기 - 특정 유저 평가하기");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "유저의 \"사용자 평가\" 조회",
            notes = "### userId와 매칭되는 특정 유저의 사용자 평가를 조회합니다.\n" +
                    "### 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/review/{targetId}")
    public SingleResult<UserReviewResponseDto> getUserReview(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                             @ApiParam(value = "조회하려는 유저의 userId", required = true) @PathVariable Long targetId)
    {
        UserReviewResponseDto userReviewResponseDto = userReviewService.getUserReview(targetId);
        return responseService.mappingSingleResult(userReviewResponseDto, "유저의 사용자 평가 조회");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "유저의 \"모든 리뷰 내용\" 조회",
            notes = "### targetId와 매칭되는 특정 유저의 모든 리뷰 내용을 조회합니다.\n"
    )
    @GetMapping(value = "/review/detail/{targetId}")
    public ListResult<UserReviewDetailResponseDto> getUserReviewDetail(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                       @ApiParam(value = "조회하려는 유저의 userId", required = true) @PathVariable Long targetId,
                                                                       @ApiParam(value = "정렬 방식", required = true) @RequestParam String sort)
    {
        List<UserReviewDetailResponseDto> userReviewDetailResponseDtoList = userReviewService.getUserReviewDetail(targetId, sort);
        return responseService.mappingListResult(userReviewDetailResponseDtoList, "유저의 모든 리뷰 내용 조회");
    }
}
