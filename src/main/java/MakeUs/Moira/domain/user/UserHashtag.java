package MakeUs.Moira.domain.user;

import MakeUs.Moira.domain.hashtag.Hashtag;

import javax.persistence.*;

@Entity
public class UserHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private Hashtag userHashtag;
}
