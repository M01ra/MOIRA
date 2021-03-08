package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProjectPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectDetail projectDetail;

    @ManyToOne
    private Position recruitPosition;

    private int recruitPositionCount;

    public ProjectPosition(ProjectDetail projectDetail, Position recruitPosition, int recruitPositionCount){
        this.projectDetail = projectDetail;
        this.recruitPosition = recruitPosition;
        this.recruitPositionCount = recruitPositionCount;
    }
}
