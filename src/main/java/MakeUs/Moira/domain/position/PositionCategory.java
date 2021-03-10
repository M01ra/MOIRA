package MakeUs.Moira.domain.position;

import javax.persistence.*;
import java.util.List;

@Entity
public class PositionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;

    @OneToMany(mappedBy = "positionCategory")
    private List<UserPosition> userPositionList;
}
