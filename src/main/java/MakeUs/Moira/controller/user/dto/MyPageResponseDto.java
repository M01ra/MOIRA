package MakeUs.Moira.controller.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyPageResponseDto {
    private String nickname;
    private String positionName;
    private String shortIntroduction;
    private String profileImageUrl;
    private int writtenPostCount;
    private int appliedPostCount;
    private int likedPostCount;

    @Builder

    public MyPageResponseDto(String nickname, String positionName, String shortIntroduction, String profileImageUrl,
                             int writtenPostCount, int appliedPostCount, int likedPostCount) {
        this.nickname = nickname;
        this.positionName = positionName;
        this.shortIntroduction = shortIntroduction;
        this.profileImageUrl = profileImageUrl;
        this.writtenPostCount = writtenPostCount;
        this.appliedPostCount = appliedPostCount;
        this.likedPostCount = likedPostCount;
    }
}
