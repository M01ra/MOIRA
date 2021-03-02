package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.position.Position;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.userReview.UserReview;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserProject {

    @Id
    private Long id;

    @ManyToOne
    private UserHistory user;

    @OneToMany
    private List<UserReview> reviews;

    @ManyToOne
    private Project project;

    @Enumerated(EnumType.STRING)
    private UserProjectRoleType roleType;

    @ManyToOne
    private Position position;

    @Enumerated(EnumType.STRING)
    private UserProjectStatus userProjectStatus;


}
