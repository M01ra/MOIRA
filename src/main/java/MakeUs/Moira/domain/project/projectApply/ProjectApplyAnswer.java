package MakeUs.Moira.domain.project.projectApply;

import MakeUs.Moira.domain.project.projectApply.ProjectApply;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class ProjectApplyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectApply projectApply;

    private String answer;

    @Builder
    public ProjectApplyAnswer(ProjectApply projectApply, String answer) {
        this.projectApply = projectApply;
        this.answer = answer;
    }
}
