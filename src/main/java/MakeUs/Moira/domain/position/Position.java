package MakeUs.Moira.domain.position;

import javax.persistence.*;

@Entity
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PositionCategory positionCategory;

    private String positionName;

}
