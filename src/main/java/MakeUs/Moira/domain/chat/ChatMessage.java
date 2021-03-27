package MakeUs.Moira.domain.chat;

import MakeUs.Moira.controller.chat.dto.ChatMessageResponseDto;
import MakeUs.Moira.controller.chat.dto.ChatMessageSendResponseDto;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ChatMessage extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User sender;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private String messageContent;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @Builder
    public ChatMessage(ChatRoom chatRoom,
                       User sender,
                       MessageType messageType,
                       String messageContent,
                       ReadStatus readStatus)
    {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.readStatus = readStatus;
    }

    public void updateChatRoom(ChatRoom chatRoom) {
        if (this.chatRoom != null) {
            this.chatRoom.getChatMessageList()
                         .remove(this);
        }
        this.chatRoom = chatRoom;
        chatRoom.getChatMessageList()
                .add(this);
    }

    public void updateReadStatus() {
        this.readStatus = ReadStatus.READ;
    }

    public ChatMessageResponseDto toChatMessageResponseDto() {

        return ChatMessageResponseDto.builder()
                                     .chatId(id)
                                     .senderId(sender.getId())
                                     .messageType(messageType.name())
                                     .messageContent(messageContent)
                                     .writtenTime(getCreatedDate())
                                     .build();
    }

    public ChatMessageSendResponseDto toChatMessageSendResponseDto(Long opponentId) {
        return ChatMessageSendResponseDto.builder()
                                         .senderId(sender.getId())
                                         .opponentId(opponentId)
                                         .messageType(messageType.name())
                                         .messageContent(messageContent)
                                         .build();
    }
}
