package MakeUs.Moira.service.userPortfolio;


import MakeUs.Moira.advice.exception.InvalidUserIdException;
import MakeUs.Moira.controller.userPortfolio.userLink.dto.UserLinkAddRequestDto;
import MakeUs.Moira.controller.userPortfolio.userLink.dto.UserLinkResponseDto;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.userLink.UserLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserLinkService {

    private final UserRepo userRepo;

    @Transactional
    public List<UserLinkResponseDto> addUserLink(Long userId, UserLinkAddRequestDto userLinkAddRequestDto) {
        User userEntity = getUserEntity(userId);
        UserPortfolio userPortfolioEntity = userEntity.getUserPortfolio();

        UserLink newUserLinkEntity = userLinkAddRequestDto.toEntity();
        newUserLinkEntity.updateUserPortfolio(userPortfolioEntity);
        userRepo.flush();

        return userEntity.getUserPortfolio()
                         .getUserLinkList()
                         .stream()
                         .map(UserLinkResponseDto::new)
                         .collect(Collectors.toList());
    }

    private User getUserEntity(Long userId) {
        return userRepo.findById(userId)
                       .orElseThrow(() -> new InvalidUserIdException("유효하지 않은 userId"));
    }
}
