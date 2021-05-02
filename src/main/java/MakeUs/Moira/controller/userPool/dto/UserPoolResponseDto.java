package MakeUs.Moira.controller.userPool.dto;


import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPoolLike.UserPoolLike;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class UserPoolResponseDto {
    /*
    - 닉네임, 포지션명, 프로필 이미지, 한 줄 소개, 관심태그들, 내가 좋아요 눌렀던가?
        - 포지션, 조회수 순 정렬
     */
    private Long         userPoolId;
    private String       nickname;
    private String       positionName;
    private String       profileImage;
    private String       shortIntroduction;
    private List<String> hashtagList;
    private boolean      isLikedByUser;

    public UserPoolResponseDto(User loginUser, UserPool userPoolPost) {
        this.userPoolId = userPoolPost.getId();
        this.nickname = userPoolPost.getUser()
                                    .getNickname();
        this.positionName = userPoolPost.getUser()
                                        .getUserPosition()
                                        .getPositionName();
        this.profileImage = userPoolPost.getUser()
                                        .getProfileImage();
        this.shortIntroduction = userPoolPost.getUser()
                                             .getShortIntroduction();
        this.hashtagList = userPoolPost.getUser()
                                       .getUserHistory()
                                       .getUserHashtags()
                                       .stream()
                                       .map(userHashtag -> userHashtag.getHashtag()
                                                                         .getHashtagName())
                                       .collect(Collectors.toList());
        this.isLikedByUser = loginUser.getUserHistory()
                                      .getUserPoolLikes()
                                      .stream()
                                      .filter(UserPoolLike::isUserPoolLiked)
                                      .anyMatch(userPoolLike -> userPoolLike.getUserPool()
                                                                            .getId()
                                                                            .equals(userPoolPost.getId()));
    }
}
