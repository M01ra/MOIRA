package MakeUs.Moira.controller.userPortfolio.userLicense.dto;

import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserLicenseResponseDto {
    private Long userLicenseId;
    private String licenseName;
    private LocalDate acquiredAt;

    public UserLicenseResponseDto(UserLicense userLicense) {
        this.userLicenseId = userLicense.getId();
        this.licenseName = userLicense.getLicenseName();
        this.acquiredAt = userLicense.getAcquiredAt();
    }
}
