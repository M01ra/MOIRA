package MakeUs.Moira.domain.project.projectApply;

import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProjectApply {

    @Id
    private Long id;

    @ManyToOne
    private ProjectDetail projectDetail;

    @ManyToOne
    private User applicant;

    @OneToMany
    private List<ProjectApplyAnswer> projectApplyAnswerList;

    @ManyToOne
    private Position position;

    @OneToMany
    private List<OptionalApplyInfo> optionalApplyInfoList;

    @Enumerated(EnumType.STRING)
    private ProjectApplyStatus projectApplyStatus;



}



