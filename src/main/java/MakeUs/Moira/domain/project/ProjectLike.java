package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.user.UserHistory;

import javax.persistence.*;

@Entity
public class ProjectLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private Project project;

    private boolean isProjectLiked;

}
