package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.project.projectDetail.ProjectDetail;
import MakeUs.Moira.domain.user.UserProject;

import javax.persistence.*;
import java.util.List;

@Entity
public class Project {

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

    @Enumerated(EnumType.STRING)
    private ProjectType projectType;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;
}
