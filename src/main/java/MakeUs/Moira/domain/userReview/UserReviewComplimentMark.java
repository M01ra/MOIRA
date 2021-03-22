package MakeUs.Moira.domain.userReview;

import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;
import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.user.UserHashtag;
import MakeUs.Moira.domain.user.UserHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class UserReviewComplimentMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserReview userReview;

    @ManyToOne
    private ComplimentMarkInfo complimentMarkInfo;


    public UserReviewComplimentMark updateUserReview(UserReview userReview) {
        if (this.userReview != null) {
            this.userReview.getUserReviewComplimentMarkList()
                           .remove(this);
        }
        this.userReview = userReview;
        userReview.getUserReviewComplimentMarkList()
                  .add(this);
        return this;
    }

    public UserReviewComplimentMark updateComplimentMarkInfo(ComplimentMarkInfo complimentMarkInfo) {
        this.complimentMarkInfo = complimentMarkInfo;
        return this;
    }
}
