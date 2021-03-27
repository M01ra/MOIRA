package MakeUs.Moira.controller.user.dto.myPageEdit;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyPageEditPositionInfoDto {
    private Long   positionCategoryId;
    private String positionCategoryName;
    private Long   positionId;
    private String positionName;

    @Builder
    public MyPageEditPositionInfoDto(Long positionCategoryId,
                                     String positionCategoryName,
                                     Long positionId,
                                     String positionName)
    {
        this.positionCategoryId = positionCategoryId;
        this.positionCategoryName = positionCategoryName;
        this.positionId = positionId;
        this.positionName = positionName;
    }
}
