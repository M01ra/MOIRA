package MakeUs.Moira.domain.project;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProjectImage {

    @Id
    private Long id;

    @ManyToOne
    private Project project;

    private String projectImageUrl;
}
