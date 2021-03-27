package MakeUs.Moira.service.userPortfolio;

import MakeUs.Moira.advice.exception.CustomException;
import MakeUs.Moira.advice.exception.ErrorCode;
import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userAward.dto.UserAwardResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userAward.UserAward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserAwardService {

    private final UserRepo userRepo;

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

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER));
    }
}
