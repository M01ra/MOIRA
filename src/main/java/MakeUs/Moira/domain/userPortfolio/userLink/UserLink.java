package MakeUs.Moira.domain.userPortfolio.userLink;

import MakeUs.Moira.domain.userPortfolio.userLink.LinkType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class UserLink {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private LinkType linkType;

    private String linkUrl;
}
