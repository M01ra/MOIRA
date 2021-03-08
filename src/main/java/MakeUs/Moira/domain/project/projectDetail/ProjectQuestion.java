package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProjectQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectDetail projectDetail;

    private String projectQuestion;

    public ProjectQuestion(ProjectDetail projectDetail, String projectQuestion){
        this.projectDetail = projectDetail;
        this.projectQuestion = projectQuestion;
    }
}
