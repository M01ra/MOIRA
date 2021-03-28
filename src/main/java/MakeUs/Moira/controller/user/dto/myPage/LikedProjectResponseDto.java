package MakeUs.Moira.controller.user.dto.myPage;



import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.user.UserProjectRoleType;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class LikedProjectResponseDto {
    private Long                     projectId;
    private String                   projectTitle;
    private String                   nickname;
    private LocalDate                writtenTime;
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
        this.writtenTime = likedProject.getCreatedDate()
                                       .toLocalDate();
        this.hitCount = likedProject.getHitCount();
        this.projectImageUrl = likedProject.getProjectImageUrl();
        this.hashtagList = likedProject.getProjectHashtagList()
                                       .stream()
                                       .map(HashtagResponseDto::new)
                                       .collect(Collectors.toList());
    }
}
