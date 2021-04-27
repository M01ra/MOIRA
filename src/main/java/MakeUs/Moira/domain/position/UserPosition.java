package MakeUs.Moira.domain.position;

import MakeUs.Moira.controller.myPageEdit.dto.MyPageEditPositionInfoDto;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class UserPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PositionCategory positionCategory;

    private String positionName;

    public MyPageEditPositionInfoDto toMyPageEditPositionInfoDto() {
        return MyPageEditPositionInfoDto.builder()
                                        .positionCategoryId(positionCategory.getId())
                                        .positionCategoryName(positionCategory.getCategoryName())
                                        .positionId(id)
                                        .positionName(positionName)
                                        .build();
    }

}
