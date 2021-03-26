package MakeUs.Moira.controller.user;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.user.dto.myPage.*;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.user.MyPageService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Api(tags = {"6-1.마이페이지"})
@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService    myPageService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService  responseService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지",
            notes = "하단 네비게이션 바를 눌렀을 때, 마이페이지의 첫 화면입니다.\n" +
                    "비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage")
    public SingleResult<MyPageResponseDto> getMyPage(
            @RequestHeader(value = "X-AUTH-TOKEN", required = true) String token) {
        // if (token == null) throw new CustomAuthorizationException("로그인이 필요한 기능");
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        MyPageResponseDto myPageResponseDto = myPageService.getMyPage(userId);
        logger.info(myPageResponseDto.toString());
        return responseService.mappingSingleResult(myPageResponseDto, "마이페이지 로드 성공");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내가 작성한 글",
            notes = "마이페이지의 내가 작성한 글을 눌렀을 때 적용합니다.\n" +
                    "비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage/written")
    public ListResult<WrittenProjectInfoResponseDto> getWrittenPost(
            @RequestHeader(value = "X-AUTH-TOKEN", required = true) String token) {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<WrittenProjectInfoResponseDto> writtenProjectInfoResponseDtoList = myPageService.getWrittenProjectList(userId);
        logger.info(writtenProjectInfoResponseDtoList.toString());
        return responseService.mappingListResult(writtenProjectInfoResponseDtoList, "내가 작성한 글 목록 불러오기 성공");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내가 지원한 글",
            notes = "마이페이지의 지원한 글을 눌렀을 때 적용합니다.\n" +
                    "비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage/applied")
    public ListResult<AppliedProjectInfoResponseDto> getAppliedPost(
            @RequestHeader(value = "X-AUTH-TOKEN", required = true) String token) {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<AppliedProjectInfoResponseDto> appliedProjectInfoResponseDtoList = myPageService.getAppliedProjectList(userId);
        logger.info(appliedProjectInfoResponseDtoList.toString());
        return responseService.mappingListResult(appliedProjectInfoResponseDtoList, "내가 지원한 글 목록 불러오기 성공");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내가 스크랩한 글 - 모집글",
            notes = "마이페이지의 스크랩을 눌렀을 때 적용합니다.\n" +
                    "비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage/like/project")
    public ListResult<LikedProjectResponseDto> getLikedProject(
            @RequestHeader(value = "X-AUTH-TOKEN", required = true) String token,
            @ApiParam(value = "포지션 카테고리 필터", required = true, allowableValues = "develop, director, designer") @RequestParam String positionCategory,
            @ApiParam(value = "정렬 방식 필터", required = true, allowableValues = "date, hit") @RequestParam String sortby) {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<LikedProjectResponseDto> likedProjectResponseDtoList = myPageService.getLikedProjectList(userId, positionCategory, sortby);
        logger.info(likedProjectResponseDtoList.toString());
        return responseService.mappingListResult(likedProjectResponseDtoList, "내가 스크랩한 글 목록 불러오기 성공");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내가 스크랩한 글 - 인재풀",
            notes = "마이페이지 - 스크랩을 눌렀을 때 적용합니다.\n" +
                    "비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage/like/pool")
    public ListResult<LikedUserPoolResponseDto> getLikedUserPool(
            @RequestHeader(value = "X-AUTH-TOKEN", required = true) String token,
            @ApiParam(value = "포지션 카테고리 필터", required = true, allowableValues = "develop, director, designer") @RequestParam String positionCategory,
            @ApiParam(value = "정렬 방식 필터", required = true, allowableValues = "date, hit, like") @RequestParam String sortby) {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<LikedUserPoolResponseDto> likedUserPoolResponseDtoList = myPageService.getLikedUserPool(userId, positionCategory, sortby);
        logger.info(likedUserPoolResponseDtoList.toString());
        return responseService.mappingListResult(likedUserPoolResponseDtoList, "내가 스크랩한 인재풀 목록 불러오기 성공");
    }
}
