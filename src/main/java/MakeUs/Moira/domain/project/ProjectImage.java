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

    // S3에 저장할 파일 명
    private String key;

    public ProjectImage(Project project, String projectImageUrl, String key){
        this.project = project;
        this.projectImageUrl = projectImageUrl;
        this.key = key;
    }
}
