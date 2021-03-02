package MakeUs.Moira.domain.userPool;

import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.user.User;

import javax.persistence.*;

import java.util.List;

@Entity
public class UserPool {

    @Id
    private Long id;

    @OneToOne
    private User user;

    private String shortComment;

    @ManyToOne
    private Position position;

    @OneToMany
    private List<UserPoolOptionalInfo> userPoolOptionalInfoList;

    @OneToMany
    private List<UserPoolOffer> userPoolOfferList;


}
