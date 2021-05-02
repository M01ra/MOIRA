package MakeUs.Moira.controller.alarm.dto;

import MakeUs.Moira.domain.alarm.AlarmType;
import MakeUs.Moira.domain.chat.ReadStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@ToString
public class AlarmResponseDto {

    @ApiModelProperty(value = "고유한 알람 Id", example = "1")
    private Long      alarmId;
    @ApiModelProperty(value = "알람 타입", example = "APPLY")
    private AlarmType alarmType;
    @ApiModelProperty(value = "리다이렉트 할 게시물의 id", example = "1")
    private Long      alarmTargetId;
    @ApiModelProperty(value = "알람 이미지", example = "이미지url")
    private String    alarmTargetImage;
    @ApiModelProperty(value = "알람 내용", example = "모집글 제목 모집...에 새 댓글이 달렸습니다.")
    private String    alarmContent;
    @ApiModelProperty(value = "알람 읽었는지 여부", example = "true")
    private boolean   isRead;
    @ApiModelProperty(value = "알람이 저장된 시간", example = "true")
    private String    writtenTime;

    @Builder
    public AlarmResponseDto(Long alarmId,
                            AlarmType alarmType,
                            Long alarmTargetId,
                            String alarmTargetImage,
                            String alarmContent,
                            ReadStatus readStatus,
                            LocalDateTime writtenTime)
    {
        this.alarmId = alarmId;
        this.alarmType = alarmType;
        this.alarmTargetId = alarmTargetId;
        this.alarmTargetImage = alarmTargetImage;
        this.alarmContent = alarmContent;
        this.isRead = enumToBoolean(readStatus);
        this.writtenTime = getTime(writtenTime);
    }

    private boolean enumToBoolean(ReadStatus readStatus) {
        return readStatus.equals(ReadStatus.READ);
    }

    private String getTime(LocalDateTime localDateTime) {
        String time;
        if (ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now())) + "년 전";
        } else if (ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now())) + "개월 전";
        } else if (ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now())) + "일 전";
        } else if (ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now())) + "시간 전";
        } else if (ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now()) >= 1) {
            time = Long.toString(ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now())) + "분 전";
        } else {
            time = "방금 전";
        }
        return time;
    }
}
