package MakeUs.Moira.service.userPortfolio;

import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import MakeUs.Moira.domain.userPortfolio.userAward.UserAwardRepo;
import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserAwardService {

    private final UserRepo      userRepo;
    private final UserAwardRepo userAwardRepo;

    @Transactional
    public List<UserAwardResponseDto> addUserAward(Long userId, UserAwardAddRequestDto userAwardAddRequestDto) {

        User userEntity = getUserEntity(userId);
        UserPortfolio userPortfolioEntity = userEntity.getUserPortfolio();

        UserAward newUserAwardEntity = userAwardAddRequestDto.toEntity();
        newUserAwardEntity.updateUserPortfolio(userPortfolioEntity);
        userRepo.flush();

        return userEntity.getUserPortfolio()
                         .getUserAwardList()
                         .stream()
                         .map(UserAwardResponseDto::new)
                         .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserAward(Long userId, Long userAwardId) {
        UserPortfolio userPortfolioEntity = getUserEntity(userId).getUserPortfolio();
        UserAward userAward = userAwardRepo.findById(userAwardId)
                                           .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_PORTFOLIO));
        userPortfolioEntity.deleteUserAward(userAward);
    }

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}
