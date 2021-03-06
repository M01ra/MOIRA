package MakeUs.Moira.controller.user.dto.signup;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PositionCategoryResponseDto {
    private Long id;
    private String positionCategoryName;

    @Builder
    public PositionCategoryResponseDto(Long id, String positionCategoryName) {
        this.id = id;
        this.positionCategoryName = positionCategoryName;
    }
}
