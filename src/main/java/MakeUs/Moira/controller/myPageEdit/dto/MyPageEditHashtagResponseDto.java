package MakeUs.Moira.controller.myPageEdit.dto;

import MakeUs.Moira.controller.myPage.dto.HashtagResponseDto;
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
