package MakeUs.Moira.controller.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponseDto {
    private Long          chatId;
    private Long          senderId;
    private String        messageType;
    private String        messageContent;
    private LocalDateTime writtenTime;

    @Builder
    public ChatMessageResponseDto(Long chatId,
                                  Long senderId,
                                  String messageType,
                                  String messageContent,
                                  LocalDateTime writtenTime)
    {
        this.chatId = chatId;
        this.senderId = senderId;
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.writtenTime = writtenTime;
    }
}
