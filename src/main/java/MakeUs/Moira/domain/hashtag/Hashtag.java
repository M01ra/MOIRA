package MakeUs.Moira.domain.hashtag;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Hashtag {

    @Id
    private Long id;

    private String hashtagName;
}
