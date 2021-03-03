package MakeUs.Moira.domain.userPool;

import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.user.User;

import javax.persistence.*;

import java.util.List;

@Entity
public class UserPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userPool")
    private User user;

    private String shortComment;

    @ManyToOne
    private Position position;

    @OneToMany(mappedBy = "userPool")
    private List<UserPoolOptionalInfo> userPoolOptionalInfoList;

    @OneToMany(mappedBy = "userPool")
    private List<UserPoolOffer> userPoolOfferList;


}
