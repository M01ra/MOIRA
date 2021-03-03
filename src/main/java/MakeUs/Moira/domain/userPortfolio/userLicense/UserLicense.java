package MakeUs.Moira.domain.userPortfolio.userLicense;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class UserLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String licenseName;

    private LocalDate acquiredAt;

}
