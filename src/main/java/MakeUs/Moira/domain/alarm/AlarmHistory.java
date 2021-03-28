package MakeUs.Moira.domain.alarm;

import MakeUs.Moira.controller.home.dto.AlarmResponseDto;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.chat.ReadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class AlarmHistory extends AuditorEntity {

    @Id
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private AlarmType type;

    private Long alarmTargetId;

    private String alarmContent;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @Builder
    public AlarmHistory(Long userId, AlarmType type, Long alarmTargetId, String alarmContent) {
        this.userId = userId;
        this.type = type;
        this.alarmTargetId = alarmTargetId;
        this.alarmContent = alarmContent;
        readStatus = ReadStatus.UNREAD;
    }

    public AlarmResponseDto toAlarmResponseDto() {
        return AlarmResponseDto.builder()
                               .alarmId(id)
                               .alarmType(type.name())
                               .alarmTargetId(alarmTargetId)
                               .alarmContent(alarmContent)
                               .readStatus(readStatus)
                               .build();
    }
}