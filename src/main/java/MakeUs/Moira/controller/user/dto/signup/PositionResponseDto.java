package MakeUs.Moira.controller.user.dto.signup;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PositionResponseDto {
    private Long positionId;
    private String positionName;

    @Builder
    public PositionResponseDto(Long positionId, String positionName) {
        this.positionId = positionId;
        this.positionName = positionName;
    }
}
