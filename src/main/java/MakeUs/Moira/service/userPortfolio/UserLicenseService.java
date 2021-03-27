package MakeUs.Moira.service.userPortfolio;


import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicenseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserLicenseService {

    private final UserRepo        userRepo;
    private final UserLicenseRepo userLicenseRepo;

    @Transactional
    public List<UserLicenseResponseDto> addUserLicense(Long userId, UserLicenseAddRequestDto userLicenseAddRequestDto) {
        User userEntity = getUserEntity(userId);
        UserPortfolio userPortfolioEntity = userEntity.getUserPortfolio();

        UserLicense newUserLicenseEntity = userLicenseAddRequestDto.toEntity();
        newUserLicenseEntity.updateUserPortfolio(userPortfolioEntity);
        userRepo.flush();

        return userEntity.getUserPortfolio()
                         .getUserLicenseList()
                         .stream()
                         .map(UserLicenseResponseDto::new)
                         .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserLicense(Long userId, Long userLicenseId) {
        UserPortfolio userPortfolioEntity = getUserEntity(userId).getUserPortfolio();
        UserLicense userLicense = userLicenseRepo.findById(userLicenseId)
                                                 .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_PORTFOLIO));
        userPortfolioEntity.deleteUserLicense(userLicense);
    }

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}
