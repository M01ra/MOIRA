package MakeUs.Moira.controller.user.dto.myPageEdit;

import MakeUs.Moira.controller.user.dto.myPage.HashtagResponseDto;
import MakeUs.Moira.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class MyPageEditProfileUpdateResponseDto {
    @ApiModelProperty(value = "닉네임")
    private String nickname;

    @ApiModelProperty(value = "포지션 카테고리")
    private String positionCategory;

    @ApiModelProperty(value = "포지션")
    private String position;

    @ApiModelProperty(value = "한 줄 소개")
    private String shortIntroduction;

    @ApiModelProperty(value = "관심태그 목록")
    private List<HashtagResponseDto> hashtagList;

    public MyPageEditProfileUpdateResponseDto(User user) {
        this.nickname = user.getNickname();
        this.positionCategory = user.getUserPosition()
                                    .getPositionCategory()
                                    .getCategoryName();
        this.position = user.getUserPosition()
                            .getPositionName();
        this.shortIntroduction = user.getShortIntroduction();
        this.hashtagList = user.getUserHistory()
                               .getUserHashtags()
                               .stream()
                               .map(HashtagResponseDto::new)
                               .collect(Collectors.toList());
    }
}
