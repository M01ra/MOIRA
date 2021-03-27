package MakeUs.Moira.controller.user.dto.myPage;



import MakeUs.Moira.domain.project.Project;
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
    private LocalDate                writtenTime;
    private int                      hitCount;
    private String                   projectImageUrl;
    private List<HashtagResponseDto> hashtagList;

    public LikedProjectResponseDto(Project likedProject) {
        this.projectId = likedProject.getId();
        this.projectTitle = likedProject.getProjectTitle();
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
