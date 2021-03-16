package MakeUs.Moira.domain.userPool;

import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class UserPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userPool")
    private User user;

    private String shortComment;

    @ManyToOne
    private UserPosition userPosition;

    @OneToMany(mappedBy = "userPool")
    private List<UserPoolOptionalInfo> userPoolOptionalInfoList = new ArrayList<>();

    @OneToMany(mappedBy = "userPool")
    private List<UserPoolOffer> userPoolOfferList = new ArrayList<>();

    public UserPool updateUser(User user) {
        this.user = user;
        return this;
    }

    public UserPool updateUserPosition(UserPosition userPosition){
        this.userPosition = userPosition;
        return this;
    }
}
