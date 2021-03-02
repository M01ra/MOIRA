package MakeUs.Moira.domain.project.projectApply;

import MakeUs.Moira.domain.project.projectApply.ProjectApply;
import MakeUs.Moira.domain.userPortfolio.UserPortfolioType;

import javax.persistence.*;

@Entity
public class OptionalApplyInfo {

    @Id
    private Long id;

    @ManyToOne
    private ProjectApply projectApply;

    @Enumerated(EnumType.STRING)
    private UserPortfolioType userPortfolioType;

    private Long UserSelectedPortfolioId;
}
