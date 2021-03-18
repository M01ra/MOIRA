package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.hashtag.Hashtag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProjectHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Hashtag projectHashtag;

    @Builder
    public ProjectHashtag(Project project, Hashtag projectHashtag) {
        this.project = project;
        this.projectHashtag = projectHashtag;
    }

    public void updateProject(Project project){
        this.project = project;
    }
}
