package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProjectQuestion {

    @Id
    private Long id;

    @ManyToOne
    private ProjectDetail projectDetail;

    private String projectQuestion;
}
