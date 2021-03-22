package MakeUs.Moira.domain.project.projectApply;

import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class ProjectApply extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectDetail projectDetail;

    @ManyToOne
    private User applicant;

    @OneToMany(mappedBy = "projectApply", cascade = CascadeType.ALL)
    private List<ProjectApplyAnswer> projectApplyAnswerList = new ArrayList<>();

    @ManyToOne
    private UserPosition userPosition;

    @OneToMany(mappedBy = "projectApply", cascade = CascadeType.ALL)
    private List<OptionalApplyInfo> optionalApplyInfoList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProjectApplyStatus projectApplyStatus;

    @Builder
    public ProjectApply(ProjectDetail projectDetail, User applicant, List<ProjectApplyAnswer> projectApplyAnswerList, UserPosition userPosition, List<OptionalApplyInfo> optionalApplyInfoList, ProjectApplyStatus projectApplyStatus) {
        this.projectDetail = projectDetail;
        this.applicant = applicant;
        this.userPosition = userPosition;
        this.projectApplyStatus = projectApplyStatus;
    }

    public void addProjectApplyAnswer(ProjectApplyAnswer projectApplyAnswer){
        this.projectApplyAnswerList.add(projectApplyAnswer);
    }

    public void addOptionalApplyInfo(OptionalApplyInfo optionalApplyInfo){
        this.optionalApplyInfoList.add(optionalApplyInfo);
    }

    public void updateProjectApplyStatus(ProjectApplyStatus projectApplyStatus){
        this.projectApplyStatus = projectApplyStatus;
    }
}



