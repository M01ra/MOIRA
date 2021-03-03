package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.hashtag.Hashtag;

import javax.persistence.*;

@Entity
public class ProjectHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Hashtag projectHashtag;
}
