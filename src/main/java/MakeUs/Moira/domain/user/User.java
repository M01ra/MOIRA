package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
public class User {

    @Id
    private Long id;

    @OneToOne
    private UserHistory userHistory;

    @OneToOne
    private UserPortfolio userPortfolio;

    @OneToOne
    private UserSecurity userSecurity;

    @OneToOne
    private UserPool userPool;

    private String realName;

    private String email;

    private String nickname;

    private String profileImage;
}
