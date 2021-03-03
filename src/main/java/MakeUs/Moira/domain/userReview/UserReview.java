package MakeUs.Moira.domain.userReview;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserProject;
import MakeUs.Moira.domain.userReview.UseReviewComplimentMark;

import javax.persistence.*;
import java.util.List;


@Entity
public class UserReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserProject userProject;

    @OneToMany(mappedBy = "userReview")
    private List<UseReviewComplimentMark> complimentMarkList;

    private int mannerPoint;

    private String reviewContent;

    @ManyToOne
    private User writtenUser;
}
