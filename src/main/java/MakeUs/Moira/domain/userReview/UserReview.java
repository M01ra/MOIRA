package MakeUs.Moira.domain.userReview;

import MakeUs.Moira.controller.userPool.dto.UserPoolDetailReviewDetailResponseDto;
import MakeUs.Moira.controller.review.dto.UserReviewDetailResponseDto;
import MakeUs.Moira.domain.AuditorEntity;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.userProject.UserProject;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Entity
public class UserReview extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserProject userProject;

    @OneToMany(mappedBy = "userReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserReviewComplimentMark> userReviewComplimentMarkList = new ArrayList<>();

    private int mannerPoint;

    private String reviewContent;

    @ManyToOne
    private User writtenUser;


    @Builder
    public UserReview(int mannerPoint, String reviewContent)
    {
        this.mannerPoint = mannerPoint;
        this.reviewContent = reviewContent;
    }

    public UserReview updateUserProject(UserProject userProject) {
        if (this.userProject != null) {
            this.userProject.getReviews()
                            .remove(this);
        }
        this.userProject = userProject;
        userProject.getReviews()
                   .add(this);
        return this;
    }

    public UserReview updateWrittenUser(User user) {
        this.writtenUser = user;
        return this;
    }

    public boolean hasComplimentMark(Long complimentMarkId) {
        return this.userReviewComplimentMarkList.stream()
                                                .anyMatch(userReviewComplimentMark -> userReviewComplimentMark.getComplimentMarkInfo()
                                                                                                              .getId()
                                                                                                              .equals(complimentMarkId));
    }

    public UserReviewDetailResponseDto toUserReviewDetailResponseDto(){
        return UserReviewDetailResponseDto.builder()
                                          .userProfileUrl(writtenUser.getProfileImage())
                                          .nickname(writtenUser.getNickname())
                                          .mannerPoint(mannerPoint)
                                          .reviewContent(reviewContent)
                                          .writtenDate(getCreatedDate().toLocalDate())
                                          .build();
    }

    public UserPoolDetailReviewDetailResponseDto toUserPoolDetailReviewDetailResponseDto(){
        return UserPoolDetailReviewDetailResponseDto.builder()
                                          .userProfileUrl(writtenUser.getProfileImage())
                                          .nickname(writtenUser.getNickname())
                                          .mannerPoint(mannerPoint)
                                          .reviewContent(reviewContent)
                                          .writtenDate(getCreatedDate().toLocalDate())
                                          .build();
    }
}
