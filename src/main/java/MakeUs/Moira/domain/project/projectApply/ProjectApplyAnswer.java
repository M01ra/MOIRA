package MakeUs.Moira.domain.project.projectApply;

import MakeUs.Moira.domain.project.projectApply.ProjectApply;

import javax.persistence.*;

@Entity
public class ProjectApplyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectApply projectApply;

    private String answer;

}
