package MakeUs.Moira.domain.userPoolOffer;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.userPool.UserPool;

import javax.persistence.*;

@Entity
public class UserPoolOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPool userPool;

    @ManyToOne
    private User suggestor;

    @Enumerated(EnumType.STRING)
    private UserPoolOfferStatus userPoolOfferStatus;
}
