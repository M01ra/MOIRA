package MakeUs.Moira.domain.project.projectApply;

import MakeUs.Moira.domain.userPortfolio.UserPortfolioType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class OptionalApplyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectApply projectApply;

    @Enumerated(EnumType.STRING)
    private UserPortfolioType userPortfolioType;

    private Long UserSelectedPortfolioId;

    @Builder
    public OptionalApplyInfo(ProjectApply projectApply, UserPortfolioType userPortfolioType, Long userSelectedPortfolioId) {
        this.projectApply = projectApply;
        this.userPortfolioType = userPortfolioType;
        this.UserSelectedPortfolioId = userSelectedPortfolioId;
    }
}
