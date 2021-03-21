package MakeUs.Moira.controller.user.dto.signup;

import MakeUs.Moira.controller.user.dto.myPage.HashtagResponseDto;
import lombok.Builder;
import lombok.Getter;


import java.util.List;


@Getter
public class SignupResponseDto {


    private Long userId;
    private String                   nickname;
    private PositionResponseDto      positionResponseDto;
    private List<HashtagResponseDto> hashtagResponseDtoList;

    @Builder
    public SignupResponseDto(Long userId, String nickname, PositionResponseDto positionResponseDto, List<HashtagResponseDto> hashtagResponseDtoList) {
        this.userId = userId;
        this.nickname = nickname;
        this.positionResponseDto = positionResponseDto;
        this.hashtagResponseDtoList = hashtagResponseDtoList;
    }
}
