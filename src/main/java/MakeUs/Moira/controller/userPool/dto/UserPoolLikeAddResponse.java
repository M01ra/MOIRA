package MakeUs.Moira.controller.userPool.dto;

import MakeUs.Moira.domain.userPoolLike.UserPoolLike;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserPoolLikeAddResponse {
    private Long userId;
    private Long userPoolId;
    private boolean isLiked;

    public UserPoolLikeAddResponse(UserPoolLike userPoolLike) {
        this.userId = userPoolLike.getUserHistory().getUser().getId();
        this.userPoolId = userPoolLike.getUserPool().getId();
        this.isLiked = userPoolLike.isUserPoolLiked();
    }
}
