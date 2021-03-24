package MakeUs.Moira.domain.userReview;

import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class UserReviewComplimentMark extends AuditorEntity {

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

    public boolean isGivenComplimentMarkId(Long complimentMarkId) {
        return this.getComplimentMarkInfo()
            .getId()
            .equals(complimentMarkId);
    }
}
