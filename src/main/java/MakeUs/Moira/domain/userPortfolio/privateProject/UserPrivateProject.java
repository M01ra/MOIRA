package MakeUs.Moira.domain.userPortfolio.privateProject;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.privateProject.PrivateProjectDuration;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserPrivateProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String projectTitle;

    private String projectSummary;

    @Enumerated(EnumType.STRING)
    private PrivateProjectDuration privateProjectDuration;

    private String ImageUrl;

    private String detailUrl;
}
