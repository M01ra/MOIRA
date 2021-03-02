package MakeUs.Moira.domain.userPortfolio.userSchool;

import MakeUs.Moira.domain.userPortfolio.*;

import javax.persistence.*;

@Entity
public class UserSchool {

    @Id
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    @ManyToOne
    private SchoolInfo schoolInfo;

    @ManyToOne
    private MajorInfo majorInfo;

    @Enumerated(EnumType.STRING )
    private DegreeType degreeType;

    @Enumerated(EnumType.STRING)
    private MakeUs.Moira.domain.userPortfolio.userSchool.userSchoolStatus userSchoolStatus;
}
