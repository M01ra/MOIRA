package MakeUs.Moira.controller.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class ChatRoomResponseDto {

    private Long      chatRoomId;
    private Long      opponentId;
    private String    opponentNickname;
    private String    opponentProfileImage;
    private String    lastMessageContent;
    private Long      unReadCount;
    private LocalDate writtenDate;

    @Builder
    public ChatRoomResponseDto(Long chatRoomId,
                               Long opponentId,
                               String opponentNickname,
                               String opponentProfileImage,
                               String lastMessageContent,
                               Long unReadCount,
                               LocalDate writtenDate)
    {
        this.chatRoomId = chatRoomId;
        this.opponentId = opponentId;
        this.opponentNickname = opponentNickname;
        this.opponentProfileImage = opponentProfileImage;
        this.lastMessageContent = lastMessageContent;
        this.unReadCount = unReadCount;
        this.writtenDate = writtenDate;
    }
}
