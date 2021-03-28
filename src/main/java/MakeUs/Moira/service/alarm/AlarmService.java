package MakeUs.Moira.service.alarm;
import MakeUs.Moira.domain.alarm.AlarmHistory;
import MakeUs.Moira.domain.alarm.AlarmHistoryRepo;
import MakeUs.Moira.domain.alarm.AlarmType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AlarmService {

    private final AlarmHistoryRepo alarmHistoryRepo;

    @Transactional
    public void saveProjectApply(String content, AlarmType alarmType, Long projectApplyId, Long userId) {
        alarmHistoryRepo.save(AlarmHistory.builder()
                                          .alarmContent(content)
                                          .type(alarmType)
                                          .alarmTargetId(projectApplyId)
                                          .userId(userId)
                                          .build());
    }

    @Transactional
    public void saveComment(String projectTitle, Long projectId, Long userId) {
        alarmHistoryRepo.save(AlarmHistory.builder()
                                          .alarmContent(projectTitle + "에 새 댓글이 달렸습니다.")
                                          .type(AlarmType.PROJECT)
                                          .alarmTargetId(projectId)
                                          .userId(userId)
                                          .build());
    }


    private String getProjectTitle(String projectTitle){
        if(projectTitle.length() > 10){
            projectTitle = projectTitle.substring(0, 10) + "...";
        }
        return projectTitle;
    }
}