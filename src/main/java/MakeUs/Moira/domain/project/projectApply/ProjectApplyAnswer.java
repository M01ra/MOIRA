package MakeUs.Moira.domain.project.projectApply;

import MakeUs.Moira.domain.project.projectApply.ProjectApply;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProjectApplyAnswer {

    @Id
    private Long id;

    @ManyToOne
    private ProjectApply projectApply;

    private String answer;

}
