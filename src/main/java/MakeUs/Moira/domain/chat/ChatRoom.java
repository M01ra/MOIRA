package MakeUs.Moira.domain.chat;

import MakeUs.Moira.controller.chat.dto.ChatRoomResponseDto;
import MakeUs.Moira.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User userA;

    @ManyToOne
    private User userB;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessageList = new ArrayList<>();


    @Builder
    public ChatRoom(User userA, User userB) {
        this.userA = userA;
        this.userB = userB;
    }

    public ChatRoomResponseDto toChatRoomResponseDto(Long userId) {

        User opponent = getOpponent(userId);
        ChatMessage lastMessage = getLastMessage(chatMessageList);

        return ChatRoomResponseDto.builder()
                                  .chatRoomId(id)
                                  .opponentId(opponent.getId())
                                  .opponentNickname(opponent.getNickname())
                                  .opponentProfileImage(opponent.getProfileImage())
                                  .lastMessageContent(lastMessage.getMessageContent())
                                  .unReadCount(countUnReadMessage(opponent))
                                  .writtenDate(lastMessage.getCreatedDate()
                                                          .toLocalDate())
                                  .build();
    }

    public void updateChatMessageReadStatus(Long userId) {
        chatMessageList.stream()
                       .filter(chatMessage -> !chatMessage.getSender()
                                                          .getId()
                                                          .equals(userId))
                       .filter(chatMessage -> chatMessage.getReadStatus() == ReadStatus.UNREAD)
                       .forEach(ChatMessage::updateReadStatus);
    }

    public boolean existUnreadMessage(Long userId) {
        User opponent = getOpponent(userId);
        return chatMessageList.stream()
                              .filter(chatMessage -> chatMessage.getReadStatus()
                                                                .equals(ReadStatus.UNREAD))
                              .anyMatch(chatMessage -> chatMessage.getSender()
                                                                  .getId()
                                                                  .equals(opponent.getId()));
    }

    private User getOpponent(Long userId) {
        if (userA.getId()
                 .equals(userId)) {
            return userB;
        }
        return userA;
    }

    private Long countUnReadMessage(User opponent) {
        return chatMessageList.stream()
                              .filter(chatMessage -> chatMessage.getReadStatus()
                                                                .equals(ReadStatus.UNREAD))
                              .filter(chatMessage -> chatMessage.getSender()
                                                                .getId()
                                                                .equals(opponent.getId()))
                              .count();
    }

    private ChatMessage getLastMessage(List<ChatMessage> chatMessageList) {
        return chatMessageList.get(chatMessageList.size() - 1);
    }


}
