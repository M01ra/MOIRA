package MakeUs.Moira.domain.alarm;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.fcm.model.FcmMessageTitleType;
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
    private FcmMessageTitleType type;

    private String alarmContent;

    @Builder
    public AlarmHistory(Long userId, String alarmContent) {
        this.userId = userId;
        this.alarmContent = alarmContent;
    }
}
