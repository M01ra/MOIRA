package MakeUs.Moira.controller.user.dto.myPageEdit;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyPageEditPositionResponseDto {
    private Long userId;
    private Long positionId;
    private String positionName;

    @Builder
    public MyPageEditPositionResponseDto(Long userId, Long positionId, String positionName) {
        this.userId = userId;
        this.positionId = positionId;
        this.positionName = positionName;
    }
}
