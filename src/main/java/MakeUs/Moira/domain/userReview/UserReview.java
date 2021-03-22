package MakeUs.Moira.domain.userReview;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserProject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@NoArgsConstructor
@Getter
@Entity
public class UserReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserProject userProject;

    @OneToMany(mappedBy = "userReview")
    private List<UserReviewComplimentMark> complimentMarkList;

    private int mannerPoint;

    private String reviewContent;

    @ManyToOne
    private User writtenUser;
}
