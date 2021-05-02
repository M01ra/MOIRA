package MakeUs.Moira.service.userPortfolio;

import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareer;
import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareerRepo;
import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserCareerService {

    private final UserRepo       userRepo;
    private final UserCareerRepo userCareerRepo;

    @Transactional
    public List<UserCareerResponseDto> addUserCareer(Long userId, UserCareerAddRequestDto userCareerAddRequestDto) {
        User userEntity = getUserEntity(userId);
        UserPortfolio userPortfolioEntity = userEntity.getUserPortfolio();

        UserCareer newUserCareerEntity = userCareerAddRequestDto.toEntity();
        newUserCareerEntity.updateUserPortfolio(userPortfolioEntity);
        userRepo.flush();

        return userEntity.getUserPortfolio()
                         .getUserCareerList()
                         .stream()
                         .map(UserCareerResponseDto::new)
                         .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserCareer(Long userId, Long userCareerId) {
        UserPortfolio userPortfolioEntity = getUserEntity(userId).getUserPortfolio();
        UserCareer userCareer = userCareerRepo.findById(userCareerId)
                                              .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_PORTFOLIO));
        userPortfolioEntity.deleteUserCareer(userCareer);
    }

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }


}
