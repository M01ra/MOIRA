package MakeUs.Moira.domain.project.projectDetail;

import MakeUs.Moira.domain.project.*;
import MakeUs.Moira.domain.project.projectApply.ProjectApply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "projectDetail")
    private Project project;

    @OneToMany(mappedBy = "projectDetail")
    private List<ProjectComment> projectCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "projectDetail")
    private List<ProjectApply> projectApplyList = new ArrayList<>();

    @OneToMany(mappedBy = "projectDetail")
    private List<ProjectQuestion> projectQuestionList;

    @OneToMany(mappedBy = "projectDetail")
    private List<ProjectPosition> projectPositionList;

    private String projectContent;

    @Enumerated(EnumType.STRING)
    private ProjectDuration projectDuration;

    @Enumerated(EnumType.STRING)
    private ProjectLocalType projectLocalType;

    public ProjectDetail(Project project, String projectContent, ProjectDuration projectDuration, ProjectLocalType projectLocalType) {
        this.project = project;
        this.projectCommentList = projectCommentList;
        this.projectApplyList = projectApplyList;
        this.projectContent = projectContent;
        this.projectDuration = projectDuration;
        this.projectLocalType = projectLocalType;
    }

    public void addComment(ProjectComment projectComment){
        projectCommentList.add(projectComment);
    }

    public void setProjectQuestionList(List<ProjectQuestion> projectQuestionList) {
        this.projectQuestionList = projectQuestionList;
    }

    public void setProjectPositionList(List<ProjectPosition> projectPositionList) {
        this.projectPositionList = projectPositionList;
    }
}
