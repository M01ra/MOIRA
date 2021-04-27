package MakeUs.Moira.controller.myPage.dto;

import MakeUs.Moira.domain.project.ProjectHashtag;
import MakeUs.Moira.domain.user.UserHashtag;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HashtagResponseDto {
    private Long   hashtagId;
    private String hashtagName;

    @Builder
    public HashtagResponseDto(Long hashtagId, String hashtagName) {
        this.hashtagId = hashtagId;
        this.hashtagName = hashtagName;
    }

    public HashtagResponseDto(ProjectHashtag projectHashtag) {
        this.hashtagId = projectHashtag.getProjectHashtag()
                                       .getId();
        this.hashtagName = projectHashtag.getProjectHashtag()
                                         .getHashtagName();
    }

    public HashtagResponseDto(UserHashtag userHashtag) {
        this.hashtagId = userHashtag.getHashtag()
                                    .getId();
        this.hashtagName = userHashtag.getHashtag()
                                      .getHashtagName();
    }
}
