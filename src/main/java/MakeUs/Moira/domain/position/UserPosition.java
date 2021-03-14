package MakeUs.Moira.domain.position;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PositionCategory positionCategory;

    private String positionName;

}
