package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;

import javax.persistence.*;

@Entity
public class ProjectQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectDetail projectDetail;

    private String projectQuestion;
}
