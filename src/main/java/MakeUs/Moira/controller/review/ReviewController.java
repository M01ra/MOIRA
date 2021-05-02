package MakeUs.Moira.controller.review;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.review.dto.ComplimentMarkInfoResponseDto;
import MakeUs.Moira.controller.review.dto.UserReviewAddRequestDto;
import MakeUs.Moira.controller.review.dto.UserReviewAddResponseDto;
import MakeUs.Moira.controller.review.dto.UserReviewDetailResponseDto;
import MakeUs.Moira.controller.review.dto.UserReviewResponseDto;
import MakeUs.Moira.service.alarm.AlarmService;
import MakeUs.Moira.service.review.ReviewService;
import MakeUs.Moira.util.response.ResponseService;
import MakeUs.Moira.util.response.model.ListResult;
import MakeUs.Moira.util.response.model.SingleResult;
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
public class ReviewController {

    private final ReviewService reviewService;
    private final ResponseService responseService;
    private final AlarmService      alarmService;
    private final JwtTokenProvider  jwtTokenProvider;
    private final Logger            logger = LoggerFactory.getLogger(this.getClass());


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
        UserReviewAddResponseDto userReviewAddResponseDto = reviewService.addUserReview(userId, userReviewAddRequestDto);
        logger.info(userReviewAddResponseDto.toString());
        alarmService.saveUserReview(userId, userReviewAddResponseDto);
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
    @GetMapping(value = "/review/{userId}")
    public SingleResult<UserReviewResponseDto> getUserReview(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                             @ApiParam(value = "조회하려는 유저의 userId", required = true) @PathVariable Long userId)
    {
        UserReviewResponseDto userReviewResponseDto = reviewService.getUserReview(userId);
        logger.info(userReviewResponseDto.toString());
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
    @GetMapping(value = "/review/detail/{userId}")
    public ListResult<UserReviewDetailResponseDto> getUserReviewDetail(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                       @ApiParam(value = "조회하려는 유저의 userId", required = true) @PathVariable Long userId,
                                                                       @ApiParam(value = "정렬 방식: date, point", allowableValues = "date, point", required = true) @RequestParam String sort)
    {
        List<UserReviewDetailResponseDto> userReviewDetailResponseDtoList = reviewService.getUserReviewDetail(userId, sort);
        logger.info(userReviewDetailResponseDtoList.toString());
        return responseService.mappingListResult(userReviewDetailResponseDtoList, "유저의 모든 리뷰 내용 조회");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "칭찬 뱃지 정보 불러오기",
            notes = "- 팀원 평가하기 - 특정 멤버 평가 - 칭찬 뱃지 정보 불러오기\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/compliment")
    public ListResult<ComplimentMarkInfoResponseDto> getComplimentMark(@RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        List<ComplimentMarkInfoResponseDto> complimentMarkInfoResponseDtoList = reviewService.getComplimentMark();
        logger.info(complimentMarkInfoResponseDtoList.toString());
        return responseService.mappingListResult(complimentMarkInfoResponseDtoList, "칭찬 뱃지 정보 불러오기");
    }
}
