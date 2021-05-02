package MakeUs.Moira.controller.chat;

import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.chat.dto.ChatMessageResponseDto;
import MakeUs.Moira.controller.chat.dto.ChatMessageSendRequestDto;
import MakeUs.Moira.controller.chat.dto.ChatMessageSendResponseDto;
import MakeUs.Moira.controller.chat.dto.ChatRoomResponseDto;
import MakeUs.Moira.service.alarm.AlarmService;
import MakeUs.Moira.service.chat.ChatService;
import MakeUs.Moira.util.response.ResponseService;
import MakeUs.Moira.util.response.model.ListResult;
import MakeUs.Moira.util.response.model.SingleResult;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"8.채팅"})
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService      chatService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final AlarmService     alarmService;
    private final Logger           logger = LoggerFactory.getLogger(this.getClass());

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "채팅 방 목록 불러오기",
            notes = "### 채팅 방 목록을 불러옵니다.\n"
    )
    @GetMapping(value = "/chatroom")
    public ListResult<ChatRoomResponseDto> getChatRoomList(@RequestHeader(value = "X-AUTH-TOKEN") String token)
    {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<ChatRoomResponseDto> chatRoomResponseDtoList = chatService.getChatRoomList(userId);
        logger.info(chatRoomResponseDtoList.toString());
        return responseService.mappingListResult(chatRoomResponseDtoList, "채팅 방 목록 불러오기");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "유저와 채팅 내용 불러오기",
            notes = "### 유저와 채팅 내용을 불러옵니다.\n" +
                    "### 페이징이 적용되며, 20개씩 쪽지를 불러옵니다. Ex) ?page=1"
    )
    @GetMapping(value = "/chatroom/{chatRoomId}")
    public ListResult<ChatMessageResponseDto> getChatMessageList(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                 @ApiParam(value = "채팅방 Id", required = true) @PathVariable Long chatRoomId,
                                                                 @ApiParam(value = "page", required = true) @RequestParam int page)
    {
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        List<ChatMessageResponseDto> chatMessageResponseDtoList = chatService.getChatMessageList(userId, chatRoomId, page);
        logger.info(chatMessageResponseDtoList.toString());
        return responseService.mappingListResult(chatMessageResponseDtoList, "유저와 채팅 내용 불러오기");
    }


    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 JWT_TOKEN",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(
            value = "유저에게 메시지 보내기",
            notes = "### 유저에게 메시지 보내기\n"
    )
    @PostMapping(value = "/message")
    public SingleResult<ChatMessageSendResponseDto> sendMessage(@RequestHeader(value = "X-AUTH-TOKEN") String token,
                                                                @ApiParam(required = true) @RequestBody ChatMessageSendRequestDto chatMessageSendRequestDto)
    {
        logger.info(chatMessageSendRequestDto.toString());
        Long userId = Long.parseLong(jwtTokenProvider.getUserPk(token));
        ChatMessageSendResponseDto chatMessageSendResponseDto = chatService.sendMessage(userId, chatMessageSendRequestDto);
        logger.info(chatMessageSendResponseDto.toString());
        alarmService.saveChatMessage(chatMessageSendResponseDto);
        return responseService.mappingSingleResult(chatMessageSendResponseDto, "채팅 보내기 성공");
    }
}
