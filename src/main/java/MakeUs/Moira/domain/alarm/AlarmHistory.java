package MakeUs.Moira.domain.alarm;

import MakeUs.Moira.domain.user.User;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Entity
public class AlarmHistory {

    @Id
    private Long id;

    private Long userId;

    private String alarmContent;

    @Builder
    public AlarmHistory(Long userId, String alarmContent) {
        this.userId = userId;
        this.alarmContent = alarmContent;
    }
}
