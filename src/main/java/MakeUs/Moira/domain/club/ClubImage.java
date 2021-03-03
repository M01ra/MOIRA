package MakeUs.Moira.domain.club;

import javax.persistence.*;

@Entity
public class ClubImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Club club;

    private String Image;
}
