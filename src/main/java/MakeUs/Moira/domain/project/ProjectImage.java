package MakeUs.Moira.domain.project;

import javax.persistence.*;

@Entity
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    private String projectImageUrl;
}
