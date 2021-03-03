package MakeUs.Moira.domain.userPool;

import MakeUs.Moira.domain.user.UserHistory;

import javax.persistence.*;

@Entity
public class UserPoolLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private UserPool userPool;

    private boolean isUserPoolLiked;
}
