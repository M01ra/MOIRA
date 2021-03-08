package MakeUs.Moira.domain.project;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    private String projectImageUrl;

    public ProjectImage(Project project, String projectImageUrl){
        this.project = project;
        this.projectImageUrl = projectImageUrl;
    }
}
