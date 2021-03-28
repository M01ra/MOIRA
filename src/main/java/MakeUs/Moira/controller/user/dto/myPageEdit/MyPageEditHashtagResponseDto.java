package MakeUs.Moira.controller.user.dto.myPageEdit;

import MakeUs.Moira.controller.user.dto.myPage.HashtagResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MyPageEditHashtagResponseDto {

    private Long                     userId;
    private List<HashtagResponseDto> hashtagList;

    @Builder

    public MyPageEditHashtagResponseDto(Long userId,
                                        List<HashtagResponseDto> hashtagList)
    {
        this.userId = userId;
        this.hashtagList = hashtagList;
    }
}
