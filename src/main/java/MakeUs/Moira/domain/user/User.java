package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserHistory userHistory;

    @OneToOne
    private UserPortfolio userPortfolio;

    @OneToOne
    private UserPool userPool;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    private String email;

    private String realName;

    private String nickname;

    private String profileImage;

    @ManyToOne
    private Position position;

    @Builder
    public User(UserHistory userHistory, UserPortfolio userPortfolio, UserPool userPool, UserRole userRole, String email, String realName, String nickname, String profileImage) {
        this.userHistory = userHistory;
        this.userPortfolio = userPortfolio;
        this.userPool = userPool;
        this.userRole = userRole;
        this.email = email;
        this.realName = realName;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void RegisterUser(){
        this.userRole = UserRole.USER;
    }
}
