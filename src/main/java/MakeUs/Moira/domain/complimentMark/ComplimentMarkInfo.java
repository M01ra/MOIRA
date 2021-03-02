package MakeUs.Moira.domain.complimentMark;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ComplimentMarkInfo {

    @Id
    private Long id;

    private String markName;

    private String content;
}
