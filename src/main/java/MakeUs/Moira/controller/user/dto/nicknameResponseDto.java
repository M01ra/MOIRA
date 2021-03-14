package MakeUs.Moira.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class nicknameResponseDto {
    private Long id;
    private String nickname;

    @Builder
    public nicknameResponseDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
