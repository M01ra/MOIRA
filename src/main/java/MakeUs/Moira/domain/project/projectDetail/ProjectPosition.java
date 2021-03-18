package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.position.UserPosition;
import lombok.Builder;
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
    private UserPosition recruitUserPosition;

    private int recruitPositionCount;

    @Builder
    public ProjectPosition(ProjectDetail projectDetail, UserPosition recruitUserPosition, int recruitPositionCount){
        this.projectDetail = projectDetail;
        this.recruitUserPosition = recruitUserPosition;
        this.recruitPositionCount = recruitPositionCount;
    }
}
