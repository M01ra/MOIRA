package MakeUs.Moira.domain.user.dto;

import MakeUs.Moira.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto {

    private String email;

    private String nickname;

    public User toEntity() {
        return User.builder() //
                .email(email)
                .nickname(nickname)
                .build();
    }
}
