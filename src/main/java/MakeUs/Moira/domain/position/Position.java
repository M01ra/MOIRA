package MakeUs.Moira.domain.position;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Position {

    @Id
    private Long id;

    @ManyToOne
    private PositionCategory positionCategory;

    private String positionName;

}
