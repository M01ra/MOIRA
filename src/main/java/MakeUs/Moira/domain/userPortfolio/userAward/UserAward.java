package MakeUs.Moira.domain.userPortfolio.userAward;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserAward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String awardName;

    private String awardContent;

}
