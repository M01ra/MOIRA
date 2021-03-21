package MakeUs.Moira.domain.project.projectApply;

import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class ProjectApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectDetail projectDetail;

    @ManyToOne
    private User applicant;

    @OneToMany(mappedBy = "projectApply")
    private List<ProjectApplyAnswer> projectApplyAnswerList;

    @ManyToOne
    private UserPosition userPosition;

    @OneToMany(mappedBy = "projectApply")
    private List<OptionalApplyInfo> optionalApplyInfoList;

    @Enumerated(EnumType.STRING)
    private ProjectApplyStatus projectApplyStatus;



}



