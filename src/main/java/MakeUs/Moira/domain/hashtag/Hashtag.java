package MakeUs.Moira.domain.hashtag;

import javax.persistence.*;

@Entity
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hashtagName;
}
