package MakeUs.Moira.domain.userPool;

import MakeUs.Moira.domain.user.User;

import javax.persistence.*;

@Entity
public class UserPoolOffer {

    @Id
    private Long id;

    @ManyToOne
    private UserPool userPool;

    @ManyToOne
    private User suggestor;

    @Enumerated(EnumType.STRING)
    private UserPoolOfferStatus userPoolOfferStatus;
}
