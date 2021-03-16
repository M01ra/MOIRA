package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.hashtag.Hashtag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class UserHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private Hashtag hashtag;

    public UserHashtag updateUserHistory(UserHistory userHistory) {
        if (this.userHistory != null) {
            this.userHistory.getUserHashtags()
                            .remove(this);
        }
        this.userHistory = userHistory;
        userHistory.getUserHashtags()
                   .add(this);
        return this;
    }

    public UserHashtag updateHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
        return this;
    }
}
