package MakeUs.Moira.controller.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@ToString
public class ChatRoomResponseDto {

    private Long      chatRoomId;
    private Long      opponentId;
    private String    opponentNickname;
    private String    opponentProfileImage;
    private String    lastMessageContent;
    private Long      unReadCount;
    private String writtenDate;

    @Builder
    public ChatRoomResponseDto(Long chatRoomId,
                               Long opponentId,
                               String opponentNickname,
                               String opponentProfileImage,
                               String lastMessageContent,
                               Long unReadCount,
                               LocalDateTime writtenDate)
    {
        this.chatRoomId = chatRoomId;
        this.opponentId = opponentId;
        this.opponentNickname = opponentNickname;
        this.opponentProfileImage = opponentProfileImage;
        this.lastMessageContent = lastMessageContent;
        this.unReadCount = unReadCount;
        this.writtenDate = getTime(writtenDate);
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
