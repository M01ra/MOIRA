package MakeUs.Moira.service.home;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.home.dto.AlarmReadStatusUpdateResponseDto;
import MakeUs.Moira.controller.home.dto.AlarmResponseDto;
import MakeUs.Moira.controller.home.dto.HomeResponseDto;
import MakeUs.Moira.domain.alarm.AlarmHistory;
import MakeUs.Moira.domain.alarm.AlarmHistoryRepo;
import MakeUs.Moira.domain.chat.ChatRoom;
import MakeUs.Moira.domain.chat.ChatRoomRepo;
import MakeUs.Moira.domain.chat.ReadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final ChatRoomRepo     chatRoomRepo;
    private final AlarmHistoryRepo alarmHistoryRepo;

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

    @Transactional
    public AlarmReadStatusUpdateResponseDto updateReadStatus(Long alarmId) {
        AlarmHistory alarmHistory = getAlarmHistory(alarmId);
        alarmHistory.updateReadStatus();
        return alarmHistory.toAlarmReadStatusUpdateResponseDto();
    }

    private AlarmHistory getAlarmHistory(Long alarmId) {
        return alarmHistoryRepo.findById(alarmId)
                               .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ALARM));
    }
}
