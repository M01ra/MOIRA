package MakeUs.Moira.domain.alarm;

import MakeUs.Moira.fcm.model.alarmType;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class AlarmHistory {

    @Id
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private alarmType type;

    private String alarmTargetId;

    private String alarmContent;

    @Builder
    public AlarmHistory(Long userId, alarmType type, String alarmTargetId, String alarmContent) {
        this.userId = userId;
        this.type = type;
        this.alarmTargetId = alarmTargetId;
        this.alarmContent = alarmContent;
    }
}
