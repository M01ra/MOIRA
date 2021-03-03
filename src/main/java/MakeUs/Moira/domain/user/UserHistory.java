package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.club.ClubLike;
import MakeUs.Moira.domain.inquiry.Inquiry;
import MakeUs.Moira.domain.project.ProjectLike;
import MakeUs.Moira.domain.report.Report;
import MakeUs.Moira.domain.userPool.UserPoolLike;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userHistory")
    private User user;

    @OneToMany(mappedBy = "userHistory")
    private List<UserHashtag> userHashtags;

    @OneToMany(mappedBy = "userHistory")
    private List<UserProject> userProjects;

    @OneToMany(mappedBy = "userHistory")
    private List<ProjectLike> projectLikes;

    @OneToMany(mappedBy = "userHistory")
    private List<UserPoolLike> userPoolLikes;

    @OneToMany(mappedBy = "userHistory")
    private List<ClubLike> clubLikes;

    @OneToMany(mappedBy = "userHistory")
    private List<Report> reports;

    @OneToMany(mappedBy = "userHistory")
    private List<Inquiry> inquiries;

    private int participationCount;

    private int completionCount;








}
