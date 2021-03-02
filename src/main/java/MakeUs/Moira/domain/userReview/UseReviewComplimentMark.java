package MakeUs.Moira.domain.userReview;

import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UseReviewComplimentMark {

    @Id
    private Long id;

    @ManyToOne
    private UserReview userReview;

    @ManyToOne
    private ComplimentMarkInfo complimentMarkInfo;
}
