package MakeUs.Moira.domain.userPortfolio.userSchool;

import MakeUs.Moira.domain.userPortfolio.*;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserSchool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
