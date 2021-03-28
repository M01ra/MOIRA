package MakeUs.Moira.service.home;

import MakeUs.Moira.controller.home.dto.AlarmResponseDto;
import MakeUs.Moira.controller.home.dto.HomeResponseDto;
import MakeUs.Moira.domain.alarm.AlarmHistory;
import MakeUs.Moira.domain.alarm.AlarmHistoryRepo;
import MakeUs.Moira.domain.chat.ChatMessageRepo;
import MakeUs.Moira.domain.chat.ChatRoom;
import MakeUs.Moira.domain.chat.ChatRoomRepo;
import MakeUs.Moira.domain.chat.ReadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HomeService {

    private ChatRoomRepo     chatRoomRepo;
    private ChatMessageRepo  chatMessageRepo;
    private AlarmHistoryRepo alarmHistoryRepo;

    public HomeResponseDto getHome(Long userId) {

        List<ChatRoom> UserChatRoomList = chatRoomRepo.findAllChatRoom(userId);
        boolean hasUnreadMessage = UserChatRoomList.stream()
                                                   .anyMatch(chatRoom -> chatRoom.existUnreadMessage(userId));

        List<AlarmHistory> alarmHistoryList = alarmHistoryRepo.findByUserId(userId);
        boolean hasUnreadAlarm = alarmHistoryList.stream()
                                                 .anyMatch(alarmHistory -> alarmHistory.getReadStatus()
                                                                                       .equals(ReadStatus.UNREAD));
        return HomeResponseDto.builder()
                              .hasUnreadAlarm(hasUnreadAlarm)
                              .hasUnreadMessage(hasUnreadMessage)
                              .build();
    }

    public List<AlarmResponseDto> getAlarm(Long userId, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        List<AlarmHistory> alarmHistoryList = alarmHistoryRepo.findByUserIdOrderByCreatedDateDesc(userId, pageable);
        return alarmHistoryList.stream()
                               .map(AlarmHistory::toAlarmResponseDto)
                               .collect(Collectors.toList());

    }
}
