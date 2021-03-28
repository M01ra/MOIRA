package MakeUs.Moira.controller.user.dto.myPageEdit;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MyPageEditHashtagRequestDto {
    private List<Long> hashtagIdList;
}
