package MakeUs.Moira.service.userPortfolio;

import MakeUs.Moira.advice.exception.InvalidUserIdException;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userCareer.dto.UserCareerResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userCareer.UserCareer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserCareerService {

    private final UserRepo userRepo;

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

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));
    }
}
