package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.club.ClubLike;
import MakeUs.Moira.domain.inquiry.Inquiry;
import MakeUs.Moira.domain.project.ProjectLike;
import MakeUs.Moira.domain.report.Report;
import MakeUs.Moira.domain.userPool.UserPoolLike;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Getter
@Entity
public class UserHistory {

    @Id
    private Long id;

    @OneToOne
    private User user;

    @OneToMany
    private List<UserHashtag> userHashtags;

    @OneToMany
    private List<UserProject> userProjects;

    @OneToMany
    private List<ProjectLike> projectLikes;

    @OneToMany
    private List<UserPoolLike> userPoolLikes;

    @OneToMany
    private List<ClubLike> clubLikes;

    @OneToMany
    private List<Report> reports;

    @OneToMany
    private List<Inquiry> inquiries;

    private int participationCount;

    private int completionCount;








}
