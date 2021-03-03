package MakeUs.Moira.domain.userPortfolio;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.userPortfolio.privateProject.UserPrivateProject;
import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import MakeUs.Moira.domain.userPortfolio.userCarrer.UserCareer;
import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import MakeUs.Moira.domain.userPortfolio.userLink.UserLink;
import MakeUs.Moira.domain.userPortfolio.userSchool.UserSchool;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "userPortfolio")
    private List<UserSchool> userSchoolList;

    @OneToMany(mappedBy = "userPortfolio")
    private List<UserCareer> userCareerList;

    @OneToMany(mappedBy = "userPortfolio")
    private List<UserLicense> userLicenseList;

    @OneToMany(mappedBy = "userPortfolio")
    private List<UserAward> userAwardList;

    @OneToMany(mappedBy = "userPortfolio")
    private List<UserLink> userLinkList;

    @OneToMany(mappedBy = "userPortfolio")
    private List<UserPrivateProject> userPrivateProjectList;

}
