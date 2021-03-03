package MakeUs.Moira.domain.complimentMark;

import javax.persistence.*;

@Entity
public class ComplimentMarkInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String markName;

    private String content;
}
