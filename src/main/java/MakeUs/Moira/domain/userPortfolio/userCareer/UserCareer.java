package MakeUs.Moira.domain.userPortfolio.userCareer;

import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userSchool.UserSchool;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
public class UserCareer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserPortfolio userPortfolio;

    private String companyName;

    private String task;

    private LocalDate startAt;

    private LocalDate endAt;

    @Builder
    public UserCareer(String companyName, String task, String startAt, String endAt) {
        this.companyName = companyName;
        this.task = task;
        updateStartAt(startAt);
        updateEndAt(endAt);
    }

    public UserCareer updateUserPortfolio(UserPortfolio userPortfolio) {
        if (this.userPortfolio != null) {
            this.userPortfolio.getUserCareerList()
                              .remove(this);
        }
        this.userPortfolio = userPortfolio;
        userPortfolio.getUserCareerList()
                     .add(this);
        return this;
    }

    public UserCareer updateStartAt(String startAt) {
        this.startAt = LocalDate.parse(startAt);
        return this;
    }

    public UserCareer updateEndAt(String endAt) {
        this.endAt = LocalDate.parse(endAt);
        return this;
    }
}
