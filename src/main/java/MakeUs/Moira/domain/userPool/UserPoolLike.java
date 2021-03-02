package MakeUs.Moira.domain.userPool;

import MakeUs.Moira.domain.user.UserHistory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserPoolLike {

    @Id
    private Long id;

    @ManyToOne
    private UserHistory user;

    @ManyToOne
    private UserPool userPool;

    private boolean isUserPoolLiked;
}
