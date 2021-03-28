package MakeUs.Moira.service.home;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.home.dto.HomeResponseDto;
import MakeUs.Moira.domain.alarm.AlarmHistory;
import MakeUs.Moira.domain.alarm.AlarmHistoryRepo;
import MakeUs.Moira.domain.chat.ChatMessage;
import MakeUs.Moira.domain.chat.ChatMessageRepo;
import MakeUs.Moira.domain.chat.ChatRoom;
import MakeUs.Moira.domain.chat.ChatRoomRepo;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeService {

    private UserRepo         userRepo;
    private ChatRoomRepo     chatRoomRepo;
    private ChatMessageRepo  chatMessageRepo;
    private AlarmHistoryRepo alarmHistoryRepo;

    public HomeResponseDto getHome(Long userId) {
        User userEntity = getuserEntity(userId);
        List<ChatRoom> UserChatRoomList = chatRoomRepo.findAllChatRoom(userId);
        boolean hasUnreadMessage = UserChatRoomList.stream()
                                                   .anyMatch(chatRoom -> chatRoom.existUnreadMessage(userId));
        return null;
    }

    private User getuserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}
