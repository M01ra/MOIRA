package MakeUs.Moira.domain.hashtag;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hashtagName;
}
