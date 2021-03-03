package MakeUs.Moira.domain.userPool;

import MakeUs.Moira.domain.userPortfolio.UserPortfolioType;

import javax.persistence.*;

@Entity
public class UserPoolOptionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPool userPool;

    @Enumerated(EnumType.STRING)
    private UserPortfolioType userPortfolioType;

    private Long UserSelectedPortfolioId;
}
