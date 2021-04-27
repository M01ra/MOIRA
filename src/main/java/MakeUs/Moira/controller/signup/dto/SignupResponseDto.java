package MakeUs.Moira.controller.signup.dto;

import MakeUs.Moira.controller.myPage.dto.HashtagResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


import java.util.List;


@Getter
@ToString
public class SignupResponseDto {


    private Long                     userId;
    private String                   nickname;
    private PositionResponseDto      positionResponseDto;
    private List<HashtagResponseDto> hashtagResponseDtoList;

    @Builder
    public SignupResponseDto(Long userId,
                             String nickname,
                             PositionResponseDto positionResponseDto,
                             List<HashtagResponseDto> hashtagResponseDtoList)
    {
        this.userId = userId;
        this.nickname = nickname;
        this.positionResponseDto = positionResponseDto;
        this.hashtagResponseDtoList = hashtagResponseDtoList;
    }
}
