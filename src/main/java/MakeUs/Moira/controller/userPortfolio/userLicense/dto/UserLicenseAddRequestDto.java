package MakeUs.Moira.controller.userPortfolio.userLicense.dto;

import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class UserLicenseAddRequestDto {

    @NotBlank(message = "licenseName에 빈 값을 넣을 수 없음")
    private String licenseName;

    @NotBlank(message = "acquiredAt에 빈 값을 넣을 수 없음")
    private String acquiredAt;

    public UserLicense toEntity() {
        return UserLicense.builder()
                .licenseName(this.licenseName)
                .acquiredAt(this.acquiredAt)
                .build();
    }
}
