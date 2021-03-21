package MakeUs.Moira.service.userPortfolio;


import MakeUs.Moira.advice.exception.InvalidUserIdException;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userLicense.dto.UserLicenseResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userLicense.UserLicense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserLicenseService {

    private final UserRepo userRepo;

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

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));
    }
}
