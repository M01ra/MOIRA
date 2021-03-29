package MakeUs.Moira.domain.alarm;

import MakeUs.Moira.controller.home.dto.AlarmResponseDto;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.chat.ReadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
                               .alarmType(type)
                               .alarmTargetId(alarmTargetId)
                               .alarmTargetImage(alarmTargetImage)
                               .alarmContent(alarmContent)
                               .readStatus(readStatus)
                               .writtenTime(getCreatedDate())
                               .build();
    }

    public void updateReadStatus() {
        this.readStatus = ReadStatus.READ;
    }
}