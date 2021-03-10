package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.hashtag.Hashtag;
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

    public ProjectHashtag(Project project, Hashtag projectHashtag) {
        this.project = project;
        this.projectHashtag = projectHashtag;
    }
}
