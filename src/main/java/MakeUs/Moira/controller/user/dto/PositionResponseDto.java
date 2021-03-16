package MakeUs.Moira.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PositionResponseDto {
    private Long positionId;
    private String positionName;

    @Builder
    public PositionResponseDto(Long positionId, String positionName) {
        this.positionId = positionId;
        this.positionName = positionName;
    }
}
