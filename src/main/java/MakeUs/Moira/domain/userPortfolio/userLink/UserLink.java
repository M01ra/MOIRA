package MakeUs.Moira.domain.userPortfolio.userLink;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userLink.LinkType;

import javax.persistence.*;

@Entity
public class UserLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    @Enumerated(EnumType.STRING)
    private LinkType linkType;

    private String linkUrl;
}
