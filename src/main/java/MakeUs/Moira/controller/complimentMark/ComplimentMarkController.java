package MakeUs.Moira.controller.complimentMark;


import MakeUs.Moira.controller.complimentMark.dto.ComplimentMarkInfoResponseDto;
import MakeUs.Moira.response.ResponseService;
import MakeUs.Moira.response.model.ListResult;
import MakeUs.Moira.service.complimentMark.ComplimentMarkInfoService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"5.유저 리뷰"})
@RequiredArgsConstructor
@RestController
public class ComplimentMarkController {

    private final ComplimentMarkInfoService complimentMarkInfoService;
    private final ResponseService           responseService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        List<ComplimentMarkInfoResponseDto> complimentMarkInfoResponseDtoList = complimentMarkInfoService.getComplimentMark();
        logger.info(complimentMarkInfoResponseDtoList.toString());
        return responseService.mappingListResult(complimentMarkInfoResponseDtoList, "칭찬 뱃지 정보 불러오기");
    }
}
