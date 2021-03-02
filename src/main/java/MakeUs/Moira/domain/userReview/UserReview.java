package MakeUs.Moira.domain.userReview;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.userReview.UseReviewComplimentMark;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
public class UserReview {

    @Id
    private Long id;

    @OneToMany
    private List<UseReviewComplimentMark> complimentMarkList;

    private int mannerPoint;

    private String reviewContent;

    @ManyToOne
    private User writtenUser;
}
