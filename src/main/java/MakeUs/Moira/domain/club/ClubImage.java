package MakeUs.Moira.domain.club;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ClubImage {


    @Id
    private Long id;

    @ManyToOne
    private Club club;

    private String Image;
}
