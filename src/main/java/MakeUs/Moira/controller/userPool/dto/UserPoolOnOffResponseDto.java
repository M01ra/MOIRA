package MakeUs.Moira.controller.userPool.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserPoolOnOffResponseDto {
    private Long    userId;
    private Long    userPoolId;
    private boolean isVisible;

    @Builder
    public UserPoolOnOffResponseDto(Long userId, Long userPoolId, boolean isVisible) {
        this.userId = userId;
        this.userPoolId = userPoolId;
        this.isVisible = isVisible;
    }
}
