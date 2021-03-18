package MakeUs.Moira.controller.user.dto;

import MakeUs.Moira.domain.project.ProjectHashtag;
import lombok.Builder;
import lombok.Getter;

@Getter
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
}
