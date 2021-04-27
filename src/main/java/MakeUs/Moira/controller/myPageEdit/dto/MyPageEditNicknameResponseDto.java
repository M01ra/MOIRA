package MakeUs.Moira.controller.myPageEdit.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyPageEditNicknameResponseDto {
    private Long userId;
    private String nickname;

    @Builder
    public MyPageEditNicknameResponseDto(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
