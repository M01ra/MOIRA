package MakeUs.Moira.domain.position;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class PositionCategory {

    @Id
    private Long id;

    private String categoryName;

    @OneToMany
    private List<Position> positionList;
}
