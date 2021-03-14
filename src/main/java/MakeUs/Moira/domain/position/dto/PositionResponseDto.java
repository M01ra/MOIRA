package MakeUs.Moira.domain.position.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PositionResponseDto {
    private Long id;
    private String positionName;

    @Builder
    public PositionResponseDto(Long id, String positionName) {
        this.id = id;
        this.positionName = positionName;
    }
}
