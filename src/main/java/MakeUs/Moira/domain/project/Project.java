package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.UserProject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
public class Project extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "project")
    private List<ProjectHashtag> projectHashtagList;

    @OneToMany(mappedBy = "project")
    private List<UserProject> userProjectList;

    private String projectImageUrl;

    private String projectTitle;

    @OneToOne
    private ProjectDetail projectDetail;

    private int likeCount = 0;

    private int hitCount = 0;

//    @Enumerated(EnumType.STRING)
//    private ProjectType projectType;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus = ProjectStatus.RECRUITING;

    public Project(List<ProjectHashtag> projectHashtagList, List<UserProject> userProjectList, String projectTitle,
                   ProjectDetail projectDetail, int likeCount, int hitCount, ProjectStatus projectStatus) {
        this.projectHashtagList = projectHashtagList;
        this.userProjectList = userProjectList;
        this.projectTitle = projectTitle;
        this.projectDetail = projectDetail;
        this.likeCount = likeCount;
        this.hitCount = hitCount;
        this.projectStatus = projectStatus;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public void setProjectHashtagList(List<ProjectHashtag> projectHashtagList) {
        this.projectHashtagList = projectHashtagList;
    }

    public void setUserProjectList(List<UserProject> userProjectList) {
        this.userProjectList = userProjectList;
    }

    public void setProjectDetail(ProjectDetail projectDetail) {
        this.projectDetail = projectDetail;
    }

    public void changeProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public void changeProjectImageUrl(String projectImageUrl) {
        this.projectImageUrl = projectImageUrl;
    }

    public void addLike() {
        likeCount++;
    }

    public void cancelLike() {
        likeCount--;
    }

    public void addHit() {
        hitCount++;
    }

    public boolean isRecruitingPositionCategory(String positionCategoryName) {
        return this.projectDetail.getProjectPositionList()
                                 .stream()
                                 .anyMatch(projectPosition -> projectPosition.getRecruitingPositionCategoryName()
                                                                             .equals(positionCategoryName));

    }
}
