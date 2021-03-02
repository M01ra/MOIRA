package MakeUs.Moira.domain.userPortfolio;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.userPortfolio.privateProject.UserPrivateProject;
import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import MakeUs.Moira.domain.userPortfolio.userCarrer.UserCareer;
import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import MakeUs.Moira.domain.userPortfolio.userLink.UserLink;
import MakeUs.Moira.domain.userPortfolio.userSchool.UserSchool;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class UserPortfolio {

    @Id
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany
    private List<UserSchool> userSchoolList;

    @OneToMany
    private List<UserCareer> userCareerList;

    @OneToMany
    private List<UserLicense> userLicenseList;

    @OneToMany
    private List<UserAward> userAwardList;

    @OneToMany
    private List<UserLink> userLinkList;

    @OneToMany
    private List<UserPrivateProject> userPrivateProjectList;

}
