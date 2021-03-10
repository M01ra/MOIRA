package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.UserProject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "project")
    private List<ProjectHashtag> projectHashtagList;

    @OneToMany(mappedBy = "project")
    private List<UserProject> userProjectList;

    @OneToMany(mappedBy = "project")
    private List<ProjectImage> projectImageList;

    private String projectTitle;

    @OneToOne
    private ProjectDetail projectDetail;

    private int likeCount;

    private int hitCount;

//    @Enumerated(EnumType.STRING)
//    private ProjectType projectType;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    public Project(List<ProjectHashtag> projectHashtagList, List<UserProject> userProjectList, List<ProjectImage> projectImageList, String projectTitle, ProjectDetail projectDetail, int likeCount, int hitCount, ProjectStatus projectStatus) {
        this.projectHashtagList = projectHashtagList;
        this.userProjectList = userProjectList;
        this.projectImageList = projectImageList;
        this.projectTitle = projectTitle;
        this.projectDetail = projectDetail;
        this.likeCount = likeCount;
        this.hitCount = hitCount;
        this.projectStatus = projectStatus;
    }
}
