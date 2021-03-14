package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.user.UserHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProjectLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private Project project;

    private boolean isProjectLiked;

    public ProjectLike(UserHistory userHistory, Project project){
        this.userHistory = userHistory;
        this.project = project;
        isProjectLiked = true;
    }

    public void changeProjectLiked(){
        isProjectLiked = !isProjectLiked;
    }

}
