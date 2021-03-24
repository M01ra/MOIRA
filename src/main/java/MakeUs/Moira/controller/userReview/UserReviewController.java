package MakeUs.Moira.controller.userReview;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.userReview.dto.UserReviewAddRequestDto;
import MakeUs.Moira.controller.userReview.dto.UserReviewAddResponseDto;
import MakeUs.Moira.response.ResponseService;

import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.userReview.UserReviewService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Api(tags = {"5.유저 리뷰"})
@RequiredArgsConstructor
@RestController
public class UserReviewController {

    private final UserReviewService userReviewService;
    private final ResponseService   responseService;
    private final JwtTokenProvider jwtTokenProvider;


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "팀원 평가하기 - 특정 유저 평가하기",
            notes = "- 팀원 평가하기 - 특정 유저 평가하기를 등록합니다.\n" +
                    "- 완료하기를 누르면 적용됩니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @PostMapping(value = "/review")
    public SingleResult<UserReviewAddResponseDto> addUserReview(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                @RequestBody UserReviewAddRequestDto userReviewAddRequestDto)
    {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        UserReviewAddResponseDto userReviewAddResponseDto = userReviewService.addUserReview(userId, userReviewAddRequestDto);
        return responseService.mappingSingleResult(userReviewAddResponseDto, "팀원 평가하기 - 특정 유저 평가하기");
    }
}
