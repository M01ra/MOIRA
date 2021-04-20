package MakeUs.Moira.domain.projectImage;

import MakeUs.Moira.domain.project.Project;
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

    // S3에 저장할 파일 명
    private String imageKey;

    public ProjectImage(Project project, String projectImageUrl, String imageKey){
        this.project = project;
        this.projectImageUrl = projectImageUrl;
        this.imageKey = imageKey;
    }
}
