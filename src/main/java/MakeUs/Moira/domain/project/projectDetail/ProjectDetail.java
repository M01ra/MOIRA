package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.project.*;
import MakeUs.Moira.domain.project.projectApply.ProjectApply;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProjectDetail {

    @Id
    private Long id;

    @OneToOne
    private Project project;

    @OneToMany
    private List<ProjectComment> projectCommentList;

    @OneToMany
    private List<ProjectApply> projectApplyList;

    @OneToMany
    private List<ProjectQuestion> projectQuestionList;

    @OneToMany
    private List<ProjectPosition> projectPositionList;

    private String projectContent;

    @Enumerated(EnumType.STRING)
    private ProjectDuration projectDuration;

}
