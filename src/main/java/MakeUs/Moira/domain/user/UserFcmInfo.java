package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class UserFcmInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String fcmToken;

    @Builder
    public UserFcmInfo(Long id, User user, String fcmToken) {
        this.user = user;
        this.fcmToken = fcmToken;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
