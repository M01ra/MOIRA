package MakeUs.Moira.controller.myPageEdit;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.myPageEdit.dto.*;

import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.response.model.SingleResult;
import MakeUs.Moira.service.myPageEdit.MyPageEditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"6-2.마이페이지-프로필 수정"})
@RequiredArgsConstructor
@RestController
public class MyPageEditController {

    private final ResponseService   responseService;
    private final JwtTokenProvider  jwtTokenProvider;
    private final MyPageEditService myPageEditService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 첫 화면",
            notes = "- 마이페이지 - 내 정보 수정하기의 첫 화면을 가져옵니다.\n" +
                    "- 비회원인 경우 에러가 발생합니다."
    )
    @GetMapping(value = "/mypage/edit")
    public SingleResult<MyPageEditResponseDto> getMyPageEdit(@RequestHeader(value = "X-AUTH-TOKEN") String token) {
        // 권한 설정은 시큐리티에서 하자
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        MyPageEditResponseDto myPageEditResponseDto = myPageEditService.getMyPageEdit(userId);
        logger.info(myPageEditResponseDto.toString());
        return responseService.mappingSingleResult(myPageEditResponseDto, "마이페이지 - 내 정보 수정하기 - 첫 화면");
    }


    // 1. 닉네임 수정 (중복검사)
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 프로필 - 닉네임 수정",
            notes = "### 마이페이지 - 내 정보 수정하기 - 프로필 - 닉네임을 수정합니다.\n"
    )
    @PutMapping(value = "/mypage/edit/nickname")
    public SingleResult<MyPageEditNicknameResponseDto> updateMyPageEditNickname(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                                @RequestBody MyPageEditNicknameRequestDto mypageEditNicknameRequestDto)
    {
        // 권한 설정은 시큐리티에서 하자
        logger.info(mypageEditNicknameRequestDto.toString());
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        MyPageEditNicknameResponseDto myPageEditNicknameResponseDto = myPageEditService.updateMyPageEditNickname(userId, mypageEditNicknameRequestDto);
        logger.info(myPageEditNicknameResponseDto.toString());
        return responseService.mappingSingleResult(myPageEditNicknameResponseDto, "마이페이지 - 내 정보 수정하기 - 프로필 - 닉네임 수정");
    }


    // 2-1. 포지션 목록
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 프로필 - 포지션 목록",
            notes = "### 마이페이지 - 내 정보 수정하기 - 프로필 - 포지션 목록을 불러옵니다.\n"
    )
    @GetMapping(value = "/mypage/edit/position")
    public ListResult<MyPageEditPositionInfoDto> getPositionList(@RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        // 권한 설정은 시큐리티에서 하자
        List<MyPageEditPositionInfoDto> myPageEditPositionInfoDtoList = myPageEditService.getPositionList();
        logger.info(myPageEditPositionInfoDtoList.toString());
        return responseService.mappingListResult(myPageEditPositionInfoDtoList, "마이페이지 - 내 정보 수정하기 - 프로필 - 포지션 목록");
    }


    // 2-2. 포지션 수정
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 프로필 - 포지션 수정",
            notes = "### 마이페이지 - 내 정보 수정하기 - 프로필 - 포지션을 수정합니다.\n"
    )
    @PutMapping(value = "/mypage/edit/position")
    public SingleResult<MyPageEditPositionResponseDto> updateMyPageEditPosition(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                                @RequestBody MyPageEditPositionRequestDto myPageEditPositionRequestDto)
    {
        // 권한 설정은 시큐리티에서 하자
        logger.info(myPageEditPositionRequestDto.toString());
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        MyPageEditPositionResponseDto myPageEditPositionResponseDto = myPageEditService.updateMyPageEditPosition(userId, myPageEditPositionRequestDto);
        logger.info(myPageEditPositionResponseDto.toString());
        return responseService.mappingSingleResult(myPageEditPositionResponseDto, "마이페이지 - 내 정보 수정하기 - 프로필 - 포지션 수정");
    }

    // 3. 한 줄 소개
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 프로필 - 한 줄 소개 수정",
            notes = "### 마이페이지 - 내 정보 수정하기 - 프로필 - 한 줄 소개를 수정합니다.\n"
    )
    @PutMapping(value = "/mypage/edit/introduction")
    public SingleResult<MyPageEditIntroductionResponseDto> updateMyPageEditIntroduction(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                                        @RequestBody MyPageEditIntroductionRequestDto myPageEditIntroductionRequestDto)
    {
        // 권한 설정은 시큐리티에서 하자
        logger.info(myPageEditIntroductionRequestDto.toString());
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        MyPageEditIntroductionResponseDto myPageEditIntroductionResponseDto = myPageEditService.updateMyPageEditIntroduction(userId, myPageEditIntroductionRequestDto);
        logger.info(myPageEditIntroductionResponseDto.toString());
        return responseService.mappingSingleResult(myPageEditIntroductionResponseDto, "마이페이지 - 내 정보 수정하기 - 프로필 - 한 줄 소개 수정");
    }


    // 4. 해시태그 리스트 수정
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 프로필 - 관심 태그 수정",
            notes = "### 마이페이지 - 내 정보 수정하기 - 프로필 - 관심 태그를 수정합니다.\n"
    )
    @PutMapping(value = "/mypage/edit/hashtag")
    public SingleResult<MyPageEditHashtagResponseDto> updateMyPageEditHashtag(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                              @RequestBody MyPageEditHashtagRequestDto myPageEditHashtagRequestDto)
    {
        // 권한 설정은 시큐리티에서 하자
        logger.info(myPageEditHashtagRequestDto.toString());
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        MyPageEditHashtagResponseDto myPageEditHashtagResponseDto = myPageEditService.updateMyPageEditHashtag(userId, myPageEditHashtagRequestDto);
        logger.info(myPageEditHashtagResponseDto.toString());
        return responseService.mappingSingleResult(myPageEditHashtagResponseDto, "마이페이지 - 내 정보 수정하기 - 프로필 - 관심 태그 수정");
    }


    // 5. 해시태그 리스트 수정
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "마이페이지 - 내 정보 수정하기 - 프로필 - 이미지 수정",
            notes = "### 마이페이지 - 내 정보 수정하기 - 프로필 - 이미지를 수정합니다.\n"
    )
    @PutMapping(value = "/mypage/edit/image")
    public SingleResult<String> updateMyPageEditImage(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                      @RequestPart MultipartFile image)
    {
        // 권한 설정은 시큐리티에서 하자
        logger.info("fileName : " + image.getOriginalFilename() + " size : " + image.getSize());
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        String imageUrl = myPageEditService.updateMyPageEditImage(userId, image);
        logger.info(imageUrl);
        return responseService.mappingSingleResult(imageUrl, "마이페이지 - 내 정보 수정하기 - 프로필 - 이미지 수정");
    }
}
