package MakeUs.Moira.controller.alarm;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.alarm.dto.AlarmReadStatusUpdateResponseDto;
import MakeUs.Moira.controller.alarm.dto.AlarmResponseDto;
import MakeUs.Moira.service.home.HomeService;
import MakeUs.Moira.util.response.ResponseService;
import MakeUs.Moira.util.response.model.ListResult;
import MakeUs.Moira.util.response.model.SingleResult;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"1.알람"})
@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final HomeService homeService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "홈화면 - 알람 목록",
            notes = "### 홈화면에서 알람 목록을 확인합니다.\n" +
                    "### 페이징이 적용되며 10개씩 목록을 가져옵니다.\n\n" +
                    "### 알람 타입으로 PROJECT, APPLY(지원서), APPLY_ANSWER(지원서 응답), INVITE_ANSWER(팀장 초대 응답), REVIEW(리뷰), CHATROOM(채팅방)이 있습니다.\n\n" +
                    "### PROJECT\n" +
                    "- alarmTargetId를 projectId로 사용하여 /project/{projectId} 팀원 모집 - 팀 모집글 상세로 리다이렉팅 시킵니다.\n" +
                    "### REVIEW\n" +
                    "- alarmTargetId를 userId로 사용하여 /review/detail/{userId} 유저의 모든 리뷰 내용 조회로 리다이렉팅 시킵니다.\n" +
                    "### CHATROOM\n" +
                    "- alarmTargetId를 chatRoomId로 사용하여 /chatroom/{chatRoomId} 유저와 채팅 내용 불러오기로 리다이렉팅 시킵니다.\n" +
                    "### APPLY\n" +
                    "- alarmTargetId를 projectApplyId로 사용하여 /apply/{projectApplyId} 마이페이지 - 지원한 글 - 지원 목록 - 지원 내역로 리다이렉팅 시킵니다.\n" +
                    "### APPLY_ANSWER\n" +
                    "- 지원자를 수락할지 거절할지 선택하여 alarmTargetId를 projectApplyId로 사용하여 /apply/{projectApplyId} 지원서의 상태를 변경을 통해 상태를 변경시킬 수 있도록 합니다.\n" +
                    "### INVITE_ANSWER\n" +
                    "- 팀장의 초대를 수락할지 거절할지 선택하여 alarmTargetId를 projectApplyId로 사용하여 /apply/{projectApplyId} 지원서의 상태를 변경을 통해 상태를 변경시킬 수 있도록 합니다."
    )
    @GetMapping("/alarm")
    public ListResult<AlarmResponseDto> getAlarm(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                 @ApiParam(value = "페이지 Ex) page=1", required = true) @RequestParam int page)
    {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<AlarmResponseDto> alarmResponseDtoList = homeService.getAlarm(userId, page);
        logger.info(alarmResponseDtoList.toString());
        return responseService.mappingListResult(alarmResponseDtoList, "홈화면 - 알람 목록");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "홈화면 - 알람목록 - 특정 알람 읽음 처리",
            notes = "### 알람으로 리다이렉션이 일어날 때, 해당 알람을 읽음 처리합니다.\n"
    )
    @PatchMapping("/alarm/{alarmId}")
    public SingleResult<AlarmReadStatusUpdateResponseDto> updateReadStatus(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                           @ApiParam(value = "읽음 처리할 alarmId", required = true) @PathVariable Long alarmId )
    {
        AlarmReadStatusUpdateResponseDto alarmReadStatusUpdateResponseDto = homeService.updateReadStatus(alarmId);
        logger.info(alarmReadStatusUpdateResponseDto.toString());
        return responseService.mappingSingleResult(alarmReadStatusUpdateResponseDto, "홈화면 - 알람목록 - 특정 알람 읽음 처리");
    }
}
