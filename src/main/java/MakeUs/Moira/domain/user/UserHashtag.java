package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.hashtag.Hashtag;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserHashtag {

    @Id
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Hashtag userHashtag;
}
