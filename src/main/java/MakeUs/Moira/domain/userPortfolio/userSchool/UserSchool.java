package MakeUs.Moira.domain.userPortfolio.userSchool;

import MakeUs.Moira.controller.userPortfolio.userSchool.dto.UserSchoolAddRequestDto;
import MakeUs.Moira.domain.userPortfolio.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor
@Getter
@Entity
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

    @Enumerated(EnumType.STRING)
    private UserSchoolStatus userSchoolStatus;

    private LocalDate startAt;

    private LocalDate endAt;

    public UserSchool(SchoolInfo schoolInfo, MajorInfo majorInfo, UserSchoolAddRequestDto userSchoolAddRequestDto){
        updateSchoolInfo(schoolInfo);
        updateMajorInfo(majorInfo);
        updateSchoolStatus(userSchoolAddRequestDto.getUserSchoolStatus());
        updateStartAt(userSchoolAddRequestDto.getStaredAt());
        updateEndAt(userSchoolAddRequestDto.getEndAt());
    }

    public UserSchool updateUserPortfolio(UserPortfolio userPortfolio) {
        if (this.userPortfolio != null) {
            this.userPortfolio.getUserSchoolList()
                              .remove(this);
        }
        this.userPortfolio = userPortfolio;
        userPortfolio.getUserSchoolList()
                     .add(this);
        return this;
    }

    public UserSchool updateSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
        return this;
    }

    public UserSchool updateMajorInfo(MajorInfo majorInfo) {
        this.majorInfo = majorInfo;
        return this;
    }

    public UserSchool updateSchoolStatus(String userSchoolStatus) {
        switch (userSchoolStatus) {
            case "ATTENDING":
                this.userSchoolStatus = UserSchoolStatus.ATTENDING;
                break;
            case "BREAK":
                this.userSchoolStatus = UserSchoolStatus.BREAK;
                break;
            case "DROP":
                this.userSchoolStatus = UserSchoolStatus.DROP;
                break;
            case "GRADUATED":
                this.userSchoolStatus = UserSchoolStatus.GRADUATED;
                break;
            case "PROSPECTIVE":
                this.userSchoolStatus = UserSchoolStatus.PROSPECTIVE;
                break;
        }
        return this;
    }

    public UserSchool updateStartAt(String startAt) {
        this.startAt = LocalDate.parse(startAt);
        return this;
    }

    public UserSchool updateEndAt(String endAt) {
        this.endAt = LocalDate.parse(endAt);
        return this;
    }
}
