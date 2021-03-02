package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.UserProject;

import javax.persistence.*;
import java.util.List;

@Entity
public class Project {

    @Id
    private Long id;

    @OneToMany
    private List<ProjectHashtag> projectHashtagList;

    @OneToMany
    private List<UserProject> userProjectList;

    @OneToMany
    private List<ProjectImage> projectImageList;

    private String projectTitle;

    @OneToOne
    private ProjectDetail projectDetail;

    private int likeCount;

    private int hitCount;

    @Enumerated(EnumType.STRING)
    private ProjectType projectType;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;
}
