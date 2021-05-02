package MakeUs.Moira.domain.report;

import MakeUs.Moira.domain.userHistory.UserHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserHistory userHistory;

    @Enumerated(EnumType.STRING)
    private ReportTargetType targetType;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;


    @Builder
    public Report(UserHistory userHistory, ReportTargetType targetType, Long targetId, ReportType reportType, ReportStatus reportStatus){
        this.userHistory = userHistory;
        this.targetType = targetType;
        this.targetId = targetId;
        this.reportType = reportType;
        this.reportStatus = reportStatus;
    }
}
