package MakeUs.Moira.domain.userPortfolio.userLicense;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
public class UserLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String licenseName;

    private LocalDate acquiredAt;

}
