package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.project.*;
import MakeUs.Moira.domain.project.projectApply.ProjectApply;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "projectDetail")
    private Project project;

    @OneToMany(mappedBy = "projectDetail")
    private List<ProjectComment> projectCommentList;

    @OneToMany(mappedBy = "projectDetail")
    private List<ProjectApply> projectApplyList;

    @OneToMany(mappedBy = "projectDetail")
    private List<ProjectQuestion> projectQuestionList;

    @OneToMany(mappedBy = "projectDetail")
    private List<ProjectPosition> projectPositionList;

    private String projectContent;

    @Enumerated(EnumType.STRING)
    private ProjectDuration projectDuration;

}
