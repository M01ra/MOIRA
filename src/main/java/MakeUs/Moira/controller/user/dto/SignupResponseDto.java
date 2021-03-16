package MakeUs.Moira.controller.user.dto;

import MakeUs.Moira.domain.hashtag.Hashtag;
import MakeUs.Moira.domain.position.UserPosition;
import lombok.Builder;
import lombok.Getter;


import java.util.List;


@Getter
public class SignupResponseDto {


    private Long userId;
    private String nickname;
    private PositionResponseDto positionResponseDto;
    private List<HashtagResponseDto> hashtagResponseDtoList;

    @Builder
    public SignupResponseDto(Long userId, String nickname, PositionResponseDto positionResponseDto, List<HashtagResponseDto> hashtagResponseDtoList) {
        this.userId = userId;
        this.nickname = nickname;
        this.positionResponseDto = positionResponseDto;
        this.hashtagResponseDtoList = hashtagResponseDtoList;
    }
}
