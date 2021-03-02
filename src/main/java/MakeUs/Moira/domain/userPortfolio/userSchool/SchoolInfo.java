package MakeUs.Moira.domain.userPortfolio.userSchool;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SchoolInfo {

    @Id
    private  Long id;

    private String schoolName;
}
