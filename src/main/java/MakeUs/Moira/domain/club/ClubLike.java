package MakeUs.Moira.domain.club;

import MakeUs.Moira.domain.user.UserHistory;

import javax.persistence.*;

@Entity
public class ClubLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private Club club;

    private boolean isClubLiked;
}
