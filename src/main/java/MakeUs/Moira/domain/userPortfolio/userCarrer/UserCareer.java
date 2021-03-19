package MakeUs.Moira.domain.userPortfolio.userCarrer;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import lombok.Getter;

import javax.persistence.*;
import java.time.Period;

@Entity
@Getter
public class UserCareer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    @ManyToOne
    private CompanyInfo companyInfo;

    private String task;

    @Enumerated(EnumType.STRING)
    private UserCarrerStatus userCarrerStatus;

    /*
     이 부분은 고려를 좀 해봐야할 것 같다...!
     */
    @Enumerated(EnumType.STRING)
    private WorkDuration workDuration;
}
