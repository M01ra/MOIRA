package MakeUs.Moira.domain.club;

import MakeUs.Moira.domain.user.UserHistory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ClubLike {

    @Id
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private Club club;

    private boolean isClubLiked;
}
