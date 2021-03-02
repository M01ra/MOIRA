package MakeUs.Moira.domain.userPortfolio.userAward;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserAward {

    @Id
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String awardName;

    private String awardContent;

}
