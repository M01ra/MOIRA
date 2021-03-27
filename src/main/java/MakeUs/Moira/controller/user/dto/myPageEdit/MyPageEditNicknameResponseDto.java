package MakeUs.Moira.controller.user.dto.myPageEdit;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyPageEditNicknameResponseDto {
    private Long userId;
    private String nickname;

    @Builder
    public MyPageEditNicknameResponseDto(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
