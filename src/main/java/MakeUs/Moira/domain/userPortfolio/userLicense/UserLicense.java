package MakeUs.Moira.domain.userPortfolio.userLicense;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class UserLicense {

    @Id
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String licenseName;

    private LocalDate acquiredAt;

}
