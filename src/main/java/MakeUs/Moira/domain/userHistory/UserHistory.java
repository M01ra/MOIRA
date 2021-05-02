package MakeUs.Moira.domain.userHistory;

import MakeUs.Moira.controller.myPage.dto.HashtagResponseDto;
import MakeUs.Moira.controller.myPageEdit.dto.MyPageEditHashtagResponseDto;
import MakeUs.Moira.domain.inquiry.Inquiry;
import MakeUs.Moira.domain.projectLike.ProjectLike;
import MakeUs.Moira.domain.report.Report;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.userProject.UserProject;
import MakeUs.Moira.domain.userHashtag.UserHashtag;
import MakeUs.Moira.domain.userPoolLike.UserPoolLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Entity
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userHistory")
    private User user;

    @OneToMany(mappedBy = "userHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHashtag> userHashtags = new ArrayList<>();

    @OneToMany(mappedBy = "userHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> userProjects = new ArrayList<>();

    @OneToMany(mappedBy = "userHistory")
    private List<ProjectLike> projectLikes = new ArrayList<>();

    @OneToMany(mappedBy = "userHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPoolLike> userPoolLikes = new ArrayList<>();

    @OneToMany(mappedBy = "userHistory")
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "userHistory")
    private List<Inquiry> inquiries = new ArrayList<>();

    private int participationCount = 0;

    private int completionCount = 0;

    public UserHistory updateUser(User user) {
        this.user = user;
        return this;
    }

    public void addUserProject(UserProject userProject) {
        userProjects.add(userProject);
    }

    public void removeUserProject(UserProject userProject) {
        userProjects.remove(userProject);
    }

    public void addProjectLike(ProjectLike projectLike) {
        projectLikes.add(projectLike);
    }

    public void removeProjectLike(ProjectLike projectLike) {
        projectLikes.remove(projectLike);
    }

    public MyPageEditHashtagResponseDto toMyPageEditHashtagResponseDto() {
        return MyPageEditHashtagResponseDto.builder()
                                           .userId(id)
                                           .hashtagList(userHashtags.stream()
                                                                    .map(userHashtag -> HashtagResponseDto.builder()
                                                                                                          .hashtagId(userHashtag.getHashtag().getId())
                                                                                                          .hashtagName(userHashtag.getHashtag().getHashtagName())
                                                                                                          .build())
                                                                    .collect(Collectors.toList()))
                                           .build();
    }

    public void addParticipationCount(){
        participationCount++;
    }

    public void addCompletionCount(){
        completionCount++;
    }
}