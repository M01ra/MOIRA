package MakeUs.Moira.domain.alarm;

import MakeUs.Moira.controller.alarm.dto.AlarmReadStatusUpdateResponseDto;
import MakeUs.Moira.controller.alarm.dto.AlarmResponseDto;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.chat.ReadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class AlarmHistory extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private AlarmType type;

    private Long alarmTargetId;

    private String alarmTargetImage;

    private String alarmContent;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @Builder
    public AlarmHistory(Long userId, AlarmType type, Long alarmTargetId, String alarmTargetImage, String alarmContent) {
        this.userId = userId;
        this.type = type;
        this.alarmTargetId = alarmTargetId;
        this.alarmTargetImage = alarmTargetImage;
        this.alarmContent = alarmContent;
        readStatus = ReadStatus.UNREAD;
    }

    public AlarmResponseDto toAlarmResponseDto() {
        return AlarmResponseDto.builder()
                               .alarmId(id)
                               .alarmType(type)
                               .alarmTargetId(alarmTargetId)
                               .alarmTargetImage(alarmTargetImage)
                               .alarmContent(alarmContent)
                               .readStatus(readStatus)
                               .writtenTime(getCreatedDate())
                               .build();
    }

    public AlarmReadStatusUpdateResponseDto toAlarmReadStatusUpdateResponseDto() {
        return AlarmReadStatusUpdateResponseDto.builder()
                                               .alarmId(id)
                                               .read(readStatusToBoolean())
                                               .build();
    }

    private boolean readStatusToBoolean() {
        return this.readStatus.equals(ReadStatus.READ);
    }

    public void updateReadStatus() {
        this.readStatus = ReadStatus.READ;
    }
}