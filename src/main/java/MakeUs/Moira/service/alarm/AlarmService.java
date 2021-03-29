package MakeUs.Moira.service.alarm;

import MakeUs.Moira.domain.alarm.AlarmHistory;
import MakeUs.Moira.domain.alarm.AlarmHistoryRepo;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.chat.dto.ChatMessageSendResponseDto;
import MakeUs.Moira.controller.userReview.dto.UserReviewAddResponseDto;
import MakeUs.Moira.domain.chat.ChatRoom;
import MakeUs.Moira.domain.chat.ChatRoomRepo;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserProject;
import MakeUs.Moira.domain.user.UserProjectRepo;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.alarm.AlarmType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AlarmService {

    private final UserRepo         userRepo;
    private final ChatRoomRepo     chatRoomRepo;
    private final AlarmHistoryRepo alarmHistoryRepo;
    private final UserProjectRepo  userProjectRepo;


    @Transactional
    public void saveChatMessage(ChatMessageSendResponseDto chatMessageSendResponseDto) {

        Long senderId = chatMessageSendResponseDto.getSenderId();
        Long opponentId = chatMessageSendResponseDto.getOpponentId();

        User sender = getUserEntity(senderId);
        User opponent = getUserEntity(opponentId);

        ChatRoom chatRoom = chatRoomRepo.findChatRoom(senderId, opponentId);

        alarmHistoryRepo.save(AlarmHistory.builder()
                                          .userId(opponentId)
                                          .type(AlarmType.CHATROOM)
                                          .alarmTargetId(chatRoom.getId())
                                          .alarmTargetImage(opponent.getProfileImage())
                                          .alarmContent(sender.getNickname() + "로부터 쪽지가 왔어요!")
                                          .build());
    }


    @Transactional
    public void saveUserReview(Long userId, UserReviewAddResponseDto userReviewAddResponseDto) {

        User writer = getUserEntity(userId);
        UserProject userProject = getUserProject(userReviewAddResponseDto);
        User opponent = userProject.getUserHistory()
                                   .getUser();

        alarmHistoryRepo.save(AlarmHistory.builder()
                                          .userId(opponent.getId())
                                          .type(AlarmType.REVIEW)
                                          .alarmTargetId(opponent.getId())
                                          .alarmTargetImage(opponent.getProfileImage())
                                          .alarmContent(writer.getNickname() + "로부터 리뷰가 추가되었어요!")
                                          .build());
    }


    @Transactional
    public void saveProjectApply(String content, AlarmType alarmType, Long projectApplyId, Long userId, String alarmTargetImage) {
        alarmHistoryRepo.save(AlarmHistory.builder()
                                          .alarmContent(content)
                                          .type(alarmType)
                                          .alarmTargetId(projectApplyId)
                                          .alarmTargetImage(alarmTargetImage)
                                          .userId(userId)
                                          .build());
    }


    @Transactional
    public void saveComment(String projectTitle, Long projectId, Long userId, String alarmTargetImage) {
        alarmHistoryRepo.save(AlarmHistory.builder()
                                          .alarmContent(projectTitle + "에 새 댓글이 달렸습니다.")
                                          .type(AlarmType.PROJECT)
                                          .alarmTargetId(projectId)
                                          .alarmTargetImage(alarmTargetImage)
                                          .userId(userId)
                                          .build());
    }


    private UserProject getUserProject(UserReviewAddResponseDto userReviewAddResponseDto) {
        return userProjectRepo.findById(userReviewAddResponseDto.getUserProjectId())
                              .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_PROJECT));
    }


    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}
