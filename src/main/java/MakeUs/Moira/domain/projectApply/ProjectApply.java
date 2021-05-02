package MakeUs.Moira.domain.projectApply;

import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.projectDetail.ProjectDetail;
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

    @ManyToOne
    private UserPosition userPosition;

    @Enumerated(EnumType.STRING)
    private ProjectApplyStatus projectApplyStatus;

    @Builder
    public ProjectApply(ProjectDetail projectDetail, User applicant, UserPosition userPosition, ProjectApplyStatus projectApplyStatus) {
        this.projectDetail = projectDetail;
        this.applicant = applicant;
        this.userPosition = userPosition;
        this.projectApplyStatus = projectApplyStatus;
    }

    public void updateProjectApplyStatus(ProjectApplyStatus projectApplyStatus){
        this.projectApplyStatus = projectApplyStatus;
    }
}



