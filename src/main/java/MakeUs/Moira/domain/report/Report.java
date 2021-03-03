package MakeUs.Moira.domain.report;

import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserHistory;

import javax.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @ManyToOne
    private User targetUser;

    private String reportContent;

    @Enumerated(EnumType.STRING)
    private reportStatus reportStatus;

}
