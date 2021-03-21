package MakeUs.Moira.controller.userPortfolio.userLicense.dto;

import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import lombok.Getter;

@Getter
public class UserLicenseAddRequestDto {

    private String licenseName;
    private String acquiredAt;

    public UserLicense toEntity() {
        return UserLicense.builder()
                .licenseName(this.licenseName)
                .acquiredAt(this.acquiredAt)
                .build();
    }
}
