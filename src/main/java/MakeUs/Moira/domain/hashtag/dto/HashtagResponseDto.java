package MakeUs.Moira.domain.hashtag.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HashtagResponseDto {
    private Long id;
    private String hashtagName;

    @Builder
    public HashtagResponseDto(Long id, String hashtagName) {
        this.id = id;
        this.hashtagName = hashtagName;
    }
}
