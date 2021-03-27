package MakeUs.Moira.service.chat;

import MakeUs.Moira.controller.chat.dto.ChatMessageResponseDto;
import MakeUs.Moira.controller.chat.dto.ChatMessageSendRequestDto;
import MakeUs.Moira.controller.chat.dto.ChatMessageSendResponseDto;
import MakeUs.Moira.controller.chat.dto.ChatRoomResponseDto;
import MakeUs.Moira.domain.chat.*;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRoomRepo    chatRoomRepo;
    private final ChatMessageRepo chatMessageRepo;
    private final UserRepo        userRepo;

    public List<ChatRoomResponseDto> getChatRoomList(Long userId) {
        List<ChatRoom> chatRoomList = chatRoomRepo.findAllChatRoom(userId);

        return chatRoomList.stream()
                           .map(chatRoom -> chatRoom.toChatRoomResponseDto(userId))
                           .collect(Collectors.toList());
    }


    @Transactional
    public List<ChatMessageResponseDto> getChatMessageList(Long userId, Long chatRoomId, int page) {

        updateChatMessageReadStatus(userId, chatRoomId);

        Pageable pageable = PageRequest.of(page - 1, 20);
        List<ChatMessage> chatMessageList = chatMessageRepo.findAllByChatRoom_IdOrderByCreatedDate(chatRoomId, pageable);

        return chatMessageList.stream()
                              .map(ChatMessage::toChatMessageResponseDto)
                              .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageSendResponseDto sendMessage(Long userId,
                                                  ChatMessageSendRequestDto chatMessageSendRequestDto)
    {
        User userEntity = getUserEntity(userId);
        Long opponentId = chatMessageSendRequestDto.getOpponentId();

        ChatRoom chatRoom = chatRoomRepo.findChatRoom(userId, opponentId);
        if (chatRoom == null) {
            chatRoom = getNewChatRoom(userId, opponentId);
        }

        ChatMessage chatMessage = chatMessageSendRequestDto.toEntity(userEntity);
        chatMessage.updateChatRoom(chatRoom);
        return chatMessage.toChatMessageSendResponseDto(opponentId);
    }


    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(IllegalArgumentException::new);
    }


    private ChatRoom getNewChatRoom(Long userId, Long opponentId) {
        User userEntity = getUserEntity(userId);
        User opponentEntity = getUserEntity(opponentId);
        return chatRoomRepo.saveAndFlush(new ChatRoom(userEntity, opponentEntity));
    }


    public void updateChatMessageReadStatus(Long userId, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepo.findById(chatRoomId)
                                        .orElseThrow(IllegalArgumentException::new);
        chatRoom.updateChatMessageReadStatus(userId);
    }
}
