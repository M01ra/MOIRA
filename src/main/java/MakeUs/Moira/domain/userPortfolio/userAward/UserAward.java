package MakeUs.Moira.domain.userPortfolio.userAward;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;

import javax.persistence.*;

@Entity
public class UserAward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String awardName;

    private String awardContent;

}
