package MakeUs.Moira.domain.userPortfolio.userLink;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class UserLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String linkUrl;

    @Builder
    public UserLink(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public UserLink updateUserPortfolio(UserPortfolio userPortfolio) {
        if (this.userPortfolio != null) {
            this.userPortfolio.getUserLinkList()
                              .remove(this);
        }
        this.userPortfolio = userPortfolio;
        userPortfolio.getUserLinkList()
                     .add(this);
        return this;
    }
}
