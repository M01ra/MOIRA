package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProjectPosition {

    @Id
    private Long id;

    @ManyToOne
    private ProjectDetail projectDetail;

    @ManyToOne
    private Position recruitPosition;

    private int recruitPositionCount;
}
