package MakeUs.Moira.controller.user;


import MakeUs.Moira.domain.hashtag.dto.HashtagResponseDto;
import MakeUs.Moira.domain.position.dto.PositionCategoryResponseDto;
import MakeUs.Moira.domain.position.dto.PositionResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.CommonResult;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.service.hashtag.HashTagService;
import MakeUs.Moira.service.position.PositionService;
import MakeUs.Moira.service.user.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = {"회원가입"})
@RequiredArgsConstructor
@RestController
public class SignupController {

    private final UserService userService;
    private final PositionService positionService;
    private final HashTagService hashTagService;
    private final ResponseService responseService;


    @ApiOperation(
            value = "닉네임 중복 검사",
            notes = "닉네임을 전달받아서 중복이 아니면\n"
                    + "성공 : 200\n"
                    + "실패: -100")
    @PostMapping(value = "/signup/nickname")
    public CommonResult isDuplicatedNickname(@ApiParam(value = "nickname", required = true) @RequestParam String nickname) {
        if (userService.isDuplicatedNickname(nickname))
            return responseService.mappingFailCommonResultOnly(-100, "중복된 닉네임입니다");
        else
            return responseService.mappingSuccessCommonResultOnly("사용가능한 닉네임입니다");
    }


    @ApiOperation(
            value = "회원 가입 시 포지션 카테고리 목록",
            notes = "회원 가입 시 포지션 카테고리 목록을 불러옵니다. => id / name / image"
    )
    @GetMapping(value = "/signup/categories")
    public ListResult<PositionCategoryResponseDto> getAllPositionCategory() {
        List<PositionCategoryResponseDto> resultList = positionService.findAllPositionCategory();
        return responseService.mappingListResult(resultList, "포지션 카테고리 목록 불러오기 성공");
    }


    @ApiOperation(
            value = "선택한 포지션 카테고리의 상세 포지션 목록",
            notes = "선택한 포지션 카테고리의 상세 포지션 목록을 불러옵니다"
    )
    @GetMapping(value = "/signup/positions")
    public ListResult<PositionResponseDto> getAllPosition(@ApiParam(value = "포지션 카테고리 id", required = true) @RequestParam Long positionCategoryId) {
        List<PositionResponseDto> resultList = positionService.findAllPosition(positionCategoryId);
        return responseService.mappingListResult(resultList, "선택한 포지션 카테고리의 상세 포지션 목록 불러오기 성공");
    }


    @ApiOperation(
            value = "모든 관심 태그 목록",
            notes = "모든 관심 태그 목록을 불러옵니다."
    )
    @GetMapping(value = "/signup/hashtags")
    public ListResult<HashtagResponseDto> getAllHashtag() {
        List<HashtagResponseDto> resultList = hashTagService.getAllHashTag();
        return responseService.mappingListResult(resultList, "모든 관심 태그 목록을 불러오기 성공");
    }

    /*
        <다음 만들어야 하는 것>
            - 닉네임 + 포지션 + 태그들 => 유저 정보에 채우기
            - User 객체에 포지션 넣기
            - UserHistory에  객체에 태그 설정해서 넣기
     */


}