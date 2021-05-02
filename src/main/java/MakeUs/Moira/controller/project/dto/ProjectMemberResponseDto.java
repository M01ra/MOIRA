package MakeUs.Moira.controller.project.dto;

import MakeUs.Moira.domain.userProject.UserProject;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProjectMemberResponseDto {
    private Long    userProjectId;
    private Long    userId;
    private String  userProfileImage;
    private String  nickname;
    private String  userProjectRole;
    private String  positionName;
    private boolean isCompletedReview;

    public ProjectMemberResponseDto(Long loginUserId, UserProject userProject)
    {
        this.userProjectId = userProject.getId();
        this.userId = userProject.getUserHistory()
                                 .getUser()
                                 .getId();
        this.userProfileImage = userProject.getUserHistory()
                                           .getUser()
                                           .getProfileImage();
        this.nickname = userProject.getUserHistory()
                                   .getUser()
                                   .getNickname();
        this.userProjectRole = userProject.getRoleType()
                                          .name();
        this.positionName = userProject.getUserPosition()
                                       .getPositionName();
        this.isCompletedReview = userProject.getReviews()
                                            .stream()
                                            .anyMatch(userReview -> userReview.getWrittenUser()
                                                                              .getId()
                                                                              .equals(loginUserId));
    }
}
