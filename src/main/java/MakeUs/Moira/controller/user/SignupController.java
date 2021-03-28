package MakeUs.Moira.controller.user;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.user.dto.signup.SignupRequestDto;
import MakeUs.Moira.controller.user.dto.myPage.HashtagResponseDto;
import MakeUs.Moira.controller.user.dto.signup.PositionCategoryResponseDto;
import MakeUs.Moira.controller.user.dto.signup.PositionResponseDto;
import MakeUs.Moira.controller.user.dto.signup.SignupResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.service.hashtag.HashTagService;
import MakeUs.Moira.service.position.PositionService;
import MakeUs.Moira.service.user.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = {"2.회원가입"})
@RequiredArgsConstructor
@RestController
public class SignupController {

    private final UserService userService;
    private final PositionService positionService;
    private final HashTagService hashTagService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ApiOperation(
            value = "닉네임 중복 검사",
            notes = "닉네임을 전달받아서 중복을 검사합니다\n"
                    + "성공 : 200\n")
    @GetMapping(value = "/signup/nickname")
    public CommonResult isDuplicatedNickname(
            @NotBlank(message = "nickname에 빈 값을 넣을 수 없음") @ApiParam(value = "nickname", required = true) @RequestParam String nickname) {
        if(userService.isDuplicatedNickname(nickname)){
            return responseService.mappingFailCommonResultOnly(441, "이미 존재하는 닉네임");
        } else {
            return responseService.mappingSuccessCommonResultOnly("사용 가능한 닉네임");
        }
    }


    @ApiOperation(
            value = "회원 가입 시 포지션 카테고리 목록",
            notes = "회원 가입 시 포지션 카테고리 목록을 불러옵니다. => id / name / image"
    )
    @GetMapping(value = "/signup/categories")
    public ListResult<PositionCategoryResponseDto> getAllPositionCategory() {
        List<PositionCategoryResponseDto> resultList = positionService.findAllPositionCategory();
        logger.info(resultList.toString());
        return responseService.mappingListResult(resultList, "포지션 카테고리 목록 불러오기 성공");
    }


    @ApiOperation(
            value = "선택한 포지션 카테고리의 상세 포지션 목록",
            notes = "선택한 포지션 카테고리의 상세 포지션 목록을 불러옵니다"
    )
    @GetMapping(value = "/signup/positions")
    public ListResult<PositionResponseDto> getAllPosition(
            @NotNull(message = "positionCategoryId에 빈 값을 넣을 수 없음") @ApiParam(value = "포지션 카테고리 id", required = true) @RequestParam Long positionCategoryId) {
        List<PositionResponseDto> resultList = positionService.findAllPosition(positionCategoryId);
        logger.info(resultList.toString());
        return responseService.mappingListResult(resultList, "선택한 포지션 카테고리의 상세 포지션 목록 불러오기 성공");
    }


    @ApiOperation(
            value = "모든 관심 태그 목록",
            notes = "모든 관심 태그 목록을 불러옵니다."
    )
    @GetMapping(value = "/signup/hashtags")
    public ListResult<HashtagResponseDto> getAllHashtag() {
        List<HashtagResponseDto> resultList = hashTagService.getAllHashTag();
        logger.info(resultList.toString());
        return responseService.mappingListResult(resultList, "모든 관심 태그 목록을 불러오기 성공");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "회원가입",
            notes = "nickname / positionId / hashtagIdList를 POST 의 Body로 부터 전달받아 회원가입합니다."
    )
    @PostMapping(value = "/signup")
    public CommonResult signup(
            @RequestHeader(value = "X-AUTH-TOKEN") String token,
            @Valid @ApiParam(required = true) @RequestBody SignupRequestDto signupRequestDto) {
        logger.info(signupRequestDto.toString());
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        String nickname = signupRequestDto.getNickname();
        Long positionId = signupRequestDto.getPositionId();
        List<Long> hashtagIdList = signupRequestDto.getHashtagIdList();

        SignupResponseDto signupResponseDto = userService.signup(userId, nickname, positionId, hashtagIdList);
        logger.info(signupResponseDto.toString());
        return responseService.mappingSingleResult(signupResponseDto, "회원가입 성공");
    }
}