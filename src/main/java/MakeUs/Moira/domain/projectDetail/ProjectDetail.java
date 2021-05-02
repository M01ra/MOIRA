package MakeUs.Moira.domain.projectDetail;

import MakeUs.Moira.domain.project.*;
import MakeUs.Moira.domain.projectApply.ProjectApply;
import MakeUs.Moira.domain.projectComment.ProjectComment;
import MakeUs.Moira.domain.projectPosition.ProjectPosition;
import lombok.*;

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

    @OneToMany(mappedBy = "projectDetail", cascade = CascadeType.ALL)
    private List<ProjectComment> projectCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "projectDetail", cascade = CascadeType.ALL)
    private List<ProjectApply> projectApplyList = new ArrayList<>();

    @OneToMany(mappedBy = "projectDetail", cascade = CascadeType.ALL)
    private List<ProjectPosition> projectPositionList = new ArrayList<>();

    private String projectContent;

    @Enumerated(EnumType.STRING)
    private ProjectDuration projectDuration;

    @Builder
    public ProjectDetail(Project project, String projectContent, ProjectDuration projectDuration) {
        this.project = project;
        this.projectContent = projectContent;
        this.projectDuration = projectDuration;
    }

    public void addProjectComment(ProjectComment projectComment){
        projectCommentList.add(projectComment);
    }

    public void addProjectApply(ProjectApply projectApply){
        projectApplyList.add(projectApply);
    }

    public void removeProjectComment(ProjectComment projectComment){
        projectCommentList.remove(projectComment);
    }

    public void removeProjectApply(ProjectApply projectApply){
        projectApplyList.remove(projectApply);
    }

    public void addProjectPosition(ProjectPosition projectPosition){
        projectPositionList.add(projectPosition);
    }

    public void updateProjectContent(String projectContent){
        this.projectContent = projectContent;
    }

}
