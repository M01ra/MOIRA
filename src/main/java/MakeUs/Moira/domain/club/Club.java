package MakeUs.Moira.domain.club;

import MakeUs.Moira.domain.user.UserHistory;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clubName;

    @OneToMany(mappedBy = "club")
    private List<ClubImage> clubImageList;

    @Enumerated(EnumType.STRING)
    private RecruitStatus recruitStatus;

    private LocalDateTime recruitStartedAt;

    private LocalDateTime recruitFinishedAt;

    private String recruitLink;

    private String recruitContent;
}
