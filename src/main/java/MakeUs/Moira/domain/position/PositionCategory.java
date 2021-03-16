package MakeUs.Moira.domain.position;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class PositionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    private String positionCategoryImage;

    @OneToMany(mappedBy = "positionCategory")
    private List<UserPosition> userPositionList;
}
