package MakeUs.Moira.domain.userPortfolio.userCarrer;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CompanyInfo {

    @Id
    private Long id;

    private String companyName;
}
