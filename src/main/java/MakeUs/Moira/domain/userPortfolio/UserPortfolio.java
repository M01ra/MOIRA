package MakeUs.Moira.domain.userPortfolio;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.userPortfolio.userPrivateProject.UserPrivateProject;
import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareer;
import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import MakeUs.Moira.domain.userPortfolio.userLink.UserLink;
import MakeUs.Moira.domain.userPortfolio.userSchool.UserSchool;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class UserPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userPortfolio")
    private User user;

    @OneToMany(mappedBy = "userPortfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchool> userSchoolList = new ArrayList<>();

    @OneToMany(mappedBy = "userPortfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCareer> userCareerList = new ArrayList<>();

    @OneToMany(mappedBy = "userPortfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLicense> userLicenseList = new ArrayList<>();

    @OneToMany(mappedBy = "userPortfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAward> userAwardList = new ArrayList<>();

    @OneToMany(mappedBy = "userPortfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLink> userLinkList = new ArrayList<>();

    @OneToMany(mappedBy = "userPortfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPrivateProject> userPrivateProjectList = new ArrayList<>();

    public UserPortfolio updateUser(User user) {
        this.user = user;
        return this;
    }

    public void deleteUserSchool(UserSchool userSchool) {
        userSchoolList.remove(userSchool);
    }

    public void deleteUserCareer(UserCareer userCareer) {
        userCareerList.remove(userCareer);
    }

    public void deleteUserLicense(UserLicense userLicense) {
        userLicenseList.remove(userLicense);
    }

    public void deleteUserAward(UserAward userAward) {
        userAwardList.remove(userAward);
    }

    public void deleteUserLink(UserLink userLink) {
        userLinkList.remove(userLink);
    }
}
