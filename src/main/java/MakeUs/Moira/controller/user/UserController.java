package MakeUs.Moira.controller.user;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.domain.hashtag.HashtagRepo;
import MakeUs.Moira.domain.user.UserHashtag;
import MakeUs.Moira.domain.user.UserHashtagRepo;
import MakeUs.Moira.domain.user.UserHistoryRepo;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"7. 유저"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(
            value = "팀 목록 - 나의 태그 리스트",
            notes = "로그인한 유저의 태그를 조회합니다\n"
    )
    @GetMapping(value = "/tag")
    public ListResult<String> getUserHashtags(
            @ApiParam(value = "JWT 토큰", required = true) @RequestHeader(value = "X-AUTH-TOKEN") String token){
        List<String> resultList = userService.getUserHashtags(Long.parseLong(jwtTokenProvider.getUserPk(token)));
        logger.info(resultList.toString());
        return responseService.mappingListResult(resultList, "나의 태그 조회 성공");
    }
}
