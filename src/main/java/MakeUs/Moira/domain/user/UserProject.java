package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.position.UserPosition;
import MakeUs.Moira.domain.project.Project;
import MakeUs.Moira.domain.userReview.UserReview;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @OneToMany(mappedBy = "userProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserReview> reviews = new ArrayList<>();

    @ManyToOne
    private Project project;

    @Enumerated(EnumType.STRING)
    private UserProjectRoleType roleType;

    @ManyToOne
    private UserPosition userPosition;

    @Enumerated(EnumType.STRING)
    private UserProjectStatus userProjectStatus;

    @Builder
    public UserProject(UserHistory userHistory, Project project, UserProjectRoleType roleType, UserPosition userPosition, UserProjectStatus userProjectStatus){
        this.userHistory = userHistory;
        this.project = project;
        this.roleType = roleType;
        this.userPosition = userPosition;
        this.userProjectStatus = userProjectStatus;
    }

}
