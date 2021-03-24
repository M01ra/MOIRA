package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.position.PositionCategory;
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
    private PositionCategory recruitUserPositionCategory;

    private int recruitPositionCount;

    @Builder
    public ProjectPosition(ProjectDetail projectDetail, PositionCategory recruitUserPositionCategory, int recruitPositionCount){
        this.projectDetail = projectDetail;
        this.recruitUserPositionCategory = recruitUserPositionCategory;
        this.recruitPositionCount = recruitPositionCount;
    }

    public String getRecruitingPositionCategoryName(){
        return this.getRecruitUserPositionCategory().getCategoryName();
    }
}
