package MakeUs.Moira.controller.user.dto;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserHistory;
import MakeUs.Moira.domain.user.UserRole;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
public class UserSearchRequestDto {
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

    @Builder
    public UserSearchRequestDto(User user) {
        this.userHistory = user.getUserHistory();
        this.userPortfolio = user.getUserPortfolio();
        this.userPool = user.getUserPool();
        this.userRole = user.getUserRole();
        this.email = user.getEmail();
        this.realName = user.getRealName();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
