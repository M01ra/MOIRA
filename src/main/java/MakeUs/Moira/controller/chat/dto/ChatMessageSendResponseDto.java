package MakeUs.Moira.controller.chat.dto;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ChatMessageSendResponseDto {
    private Long      senderId;
    private Long      opponentId;
    private String    messageType;
    private String    messageContent;


    @Builder
    public ChatMessageSendResponseDto(Long senderId,
                                      Long opponentId,
                                      String messageType,
                                      String messageContent)
    {
        this.senderId = senderId;
        this.opponentId = opponentId;
        this.messageType = messageType;
        this.messageContent = messageContent;
    }
}
