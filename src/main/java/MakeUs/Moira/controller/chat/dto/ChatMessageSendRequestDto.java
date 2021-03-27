package MakeUs.Moira.controller.chat.dto;

import MakeUs.Moira.domain.chat.ChatMessage;
import MakeUs.Moira.domain.chat.MessageType;
import MakeUs.Moira.domain.chat.ReadStatus;
import MakeUs.Moira.domain.user.User;
import lombok.Getter;

@Getter
public class ChatMessageSendRequestDto {
    private Long   opponentId;
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
        throw new IllegalArgumentException();
    }
}
