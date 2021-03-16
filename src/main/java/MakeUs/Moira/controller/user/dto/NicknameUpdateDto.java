package MakeUs.Moira.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NicknameUpdateDto {
    private Long userId;
    private String nickname;

    @Builder
    public NicknameUpdateDto(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
