package MakeUs.Moira.domain.userPortfolio.userAward;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor @Entity
@Getter
public class UserAward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String awardName;

    private String awardContent;

    @Builder
    public UserAward(String awardName, String awardContent) {
        this.awardName = awardName;
        this.awardContent = awardContent;
    }

    public UserAward updateUserPortfolio(UserPortfolio userPortfolio) {
        if (this.userPortfolio != null) {
            this.userPortfolio.getUserAwardList()
                              .remove(this);
        }
        this.userPortfolio = userPortfolio;
        userPortfolio.getUserAwardList()
                     .add(this);
        return this;
    }
}
