package MakeUs.Moira.controller.chat.dto;

import MakeUs.Moira.domain.chat.ChatMessage;
import MakeUs.Moira.domain.chat.MessageType;
import MakeUs.Moira.domain.chat.ReadStatus;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChatMessageSendRequestDto {
    private Long   opponentId;
    @ApiModelProperty(value = "TEXT, IMAGE", example = "TEXT")
    private String messageType;
    private String messageContent;

    public ChatMessage toEntity(User sender) {
        return ChatMessage.builder()
                          .sender(sender)
                          .messageType(getMessageType(messageType))
                          .messageContent(messageContent)
                          .readStatus(ReadStatus.UNREAD)
                          .build();
    }

    public MessageType getMessageType(String messageType) {
        if (messageType.equals(MessageType.IMAGE.name())) {
            return MessageType.IMAGE;
        } else if (messageType.equals(MessageType.TEXT.name())) {
            return MessageType.TEXT;
        }
        throw new CustomException(ErrorCode.INVALID_MESSAGE_TYPE);
    }
}
