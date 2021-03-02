package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.user.UserHistory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProjectLike {

    @Id
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private Project project;

    private boolean isProjectLiked;

}
