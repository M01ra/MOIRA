package MakeUs.Moira.controller.user.dto.myPageEdit;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyPageEditIntroductionResponseDto {
    private Long userId;
    private String shortIntroduction;

    @Builder
    public MyPageEditIntroductionResponseDto(Long userId, String shortIntroduction) {
        this.userId = userId;
        this.shortIntroduction = shortIntroduction;
    }
}
