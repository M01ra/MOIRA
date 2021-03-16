package MakeUs.Moira.controller.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class PositionCategoryResponseDto {
    private Long id;
    private String positionCategoryName;
    private String positionCategoryImage;

    @Builder
    public PositionCategoryResponseDto(Long id, String positionCategoryName, String positionCategoryImage) {
        this.id = id;
        this.positionCategoryName = positionCategoryName;
        this.positionCategoryImage = positionCategoryImage;
    }
}
