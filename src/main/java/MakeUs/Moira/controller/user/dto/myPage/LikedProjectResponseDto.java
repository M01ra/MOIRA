package MakeUs.Moira.controller.user.dto.myPage;



import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.user.UserProjectRoleType;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class LikedProjectResponseDto {
    private Long                     projectId;
    private String                   projectTitle;
    private String                   nickname;
    private String                   writtenTime;
    private int                      hitCount;
    private String                   projectImageUrl;
    private List<HashtagResponseDto> hashtagList;

    public LikedProjectResponseDto(Project likedProject) {
        String nickname = likedProject.getUserProjectList().stream()
                    .filter(userProject -> userProject.getRoleType() == UserProjectRoleType.LEADER)
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.NON_EXIST_PROJECT_LEADER))
                    .getUserHistory()
                    .getUser()
                    .getNickname();


        this.projectId = likedProject.getId();
        this.projectTitle = likedProject.getProjectTitle();
        this.nickname = nickname;
        this.writtenTime = getTime(likedProject.getCreatedDate());
        this.hitCount = likedProject.getHitCount();
        this.projectImageUrl = likedProject.getProjectImageUrl();
        this.hashtagList = likedProject.getProjectHashtagList()
                                       .stream()
                                       .map(HashtagResponseDto::new)
                                       .collect(Collectors.toList());
    }

    private String getTime(LocalDateTime localDateTime){
        String time;
        if(ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.YEARS.between(localDateTime, LocalDateTime.now())) + "년 전";
        }
        else if(ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.MONTHS.between(localDateTime, LocalDateTime.now())) + "개월 전";
        }
        else if(ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.DAYS.between(localDateTime, LocalDateTime.now())) + "일 전";
        }
        else if(ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.HOURS.between(localDateTime, LocalDateTime.now())) + "시간 전";
        }
        else if(ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now()) >= 1){
            time = Long.toString(ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now())) + "분 전";
        }
        else {
            time = "방금 전";
        }
        return time;
    }
}
