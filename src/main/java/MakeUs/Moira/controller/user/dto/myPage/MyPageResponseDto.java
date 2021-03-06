package MakeUs.Moira.controller.user.dto.myPage;

import MakeUs.Moira.domain.user.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MyPageResponseDto {
    private Long   userId;
    private String nickname;
    private String positionName;
    private String shortIntroduction;
    private String profileImageUrl;
    private int    writtenPostCount;
    private int    appliedPostCount;
    private int    likedPostCount;


    public MyPageResponseDto(User user, int writtenPostCount, int appliedPostCount, int likedPostCount) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.positionName = user.getUserPosition()
                                .getPositionName();
        this.shortIntroduction = user.getShortIntroduction();
        this.profileImageUrl = user.getProfileImage();
        this.writtenPostCount = writtenPostCount;
        this.appliedPostCount = appliedPostCount;
        this.likedPostCount = likedPostCount;
    }
}
