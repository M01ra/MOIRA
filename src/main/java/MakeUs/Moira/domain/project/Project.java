package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.UserProject;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
public class Project extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectHashtag> projectHashtagList = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<UserProject> userProjectList = new ArrayList<>();

    private String projectImageUrl;

    private String projectImageKey;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectImage> projectImageList = new ArrayList<>();

    private String projectTitle;

    @OneToOne(cascade = CascadeType.ALL)
    private ProjectDetail projectDetail;

    private int likeCount = 0;

    private int hitCount = 0;

    public void addProjectHashtagList(ProjectHashtag projectHashtag) {
        projectHashtagList.add(projectHashtag);
    }

    public void addUserProjectList(UserProject userProject) {
        userProjectList.add(userProject);
    }

    public void updateProjectDetail(ProjectDetail projectDetail) {
        this.projectDetail = projectDetail;
    }

    public void updateProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public void updateProjectImageUrl(String projectImageUrl) {
        this.projectImageUrl = projectImageUrl;
    }

    public void updateProjectImageKey(String projectImageKey) {
        this.projectImageKey = projectImageKey;
    }

    public void addProjectImage(ProjectImage projectImage) {
        projectImageList.add(projectImage);
    }

    public List<String> getProjectImageUrlList() {
        List<String> imageUrlList = new ArrayList<>();
        if(projectImageUrl != null){
            imageUrlList.add(projectImageUrl);
        }
        projectImageList
                .forEach(projectImage -> imageUrlList.add(projectImage.getProjectImageUrl()));
        return imageUrlList;
    }

    public Project updateProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
        return this;
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

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus = ProjectStatus.RECRUITING;

    //    @Enumerated(EnumType.STRING)
    //    private ProjectType projectType;

    public boolean isRecruitingPositionCategory(String positionCategoryName) {
        return this.projectDetail.getProjectPositionList()
                                 .stream()
                                 .anyMatch(projectPosition -> projectPosition.getRecruitingPositionCategoryName()
                                                                             .equals(positionCategoryName));

    }
}
