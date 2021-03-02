package MakeUs.Moira.domain.project;

import MakeUs.Moira.domain.hashtag.Hashtag;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProjectHashtag {

    @Id
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Hashtag projectHashtag;
}
