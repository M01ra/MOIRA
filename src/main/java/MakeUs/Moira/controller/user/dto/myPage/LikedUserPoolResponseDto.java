package MakeUs.Moira.controller.user.dto.myPage;

import MakeUs.Moira.domain.userPool.UserPool;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class LikedUserPoolResponseDto {
    private Long                     userPoolId;
    private String                   nickname;
    private String                   positionName;
    private String                   profileImage;
    private String                   shortIntroduction;
    private List<HashtagResponseDto> hashtagList;

    public LikedUserPoolResponseDto(UserPool likedUserPool)
    {
        this.userPoolId = likedUserPool.getId();
        this.nickname = likedUserPool.getUser()
                                     .getNickname();
        this.positionName = likedUserPool.getUser()
                                         .getUserPosition()
                                         .getPositionName();
        this.profileImage = likedUserPool.getUser()
                                         .getProfileImage();
        this.shortIntroduction = likedUserPool.getUser()
                                              .getShortIntroduction();
        this.hashtagList = likedUserPool.getUser()
                                        .getUserHistory()
                                        .getUserHashtags()
                                        .stream()
                                        .map(HashtagResponseDto::new)
                                        .collect(Collectors.toList());
    }
}
