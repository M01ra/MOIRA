package MakeUs.Moira.domain.userReview;

import MakeUs.Moira.domain.complimentMark.ComplimentMarkInfo;

import javax.persistence.*;

@Entity
public class UserReviewComplimentMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserReview userReview;

    @ManyToOne
    private ComplimentMarkInfo complimentMarkInfo;
}
