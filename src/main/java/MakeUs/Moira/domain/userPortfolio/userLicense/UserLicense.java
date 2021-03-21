package MakeUs.Moira.domain.userPortfolio.userLicense;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
public class UserLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String licenseName;

    private LocalDate acquiredAt;


    @Builder
    public UserLicense(String licenseName, String acquiredAt) {
        this.licenseName = licenseName;
        updateAcquiredAt(acquiredAt);
    }

    public UserLicense updateUserPortfolio(UserPortfolio userPortfolio) {
        if (this.userPortfolio != null) {
            this.userPortfolio.getUserLicenseList()
                              .remove(this);
        }
        this.userPortfolio = userPortfolio;
        userPortfolio.getUserLicenseList()
                     .add(this);
        return this;
    }

    public UserLicense updateAcquiredAt(String acquiredAt) {
        this.acquiredAt = LocalDate.parse(acquiredAt);
        return this;
    }
}
