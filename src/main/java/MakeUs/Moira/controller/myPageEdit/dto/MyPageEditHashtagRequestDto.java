package MakeUs.Moira.controller.myPageEdit.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MyPageEditHashtagRequestDto {
    private List<Long> hashtagIdList;
}
