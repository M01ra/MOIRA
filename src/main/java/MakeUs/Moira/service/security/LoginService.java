package MakeUs.Moira.service.security;


import MakeUs.Moira.domain.security.token.TokenProvider;
import MakeUs.Moira.domain.security.token.TokenProviderFactory;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPool.UserPoolRepo;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.domain.userPortfolio.UserPortfolioRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class LoginService {

    private final TokenProviderFactory tokenProviderFactory;
    private final UserRepo userRepo;


    public String getUserSocialId(String providerName, String accessToken) {

        TokenProvider tokenProvider = tokenProviderFactory.getTokenProvider(providerName);
        return tokenProvider.getUserSocialId(accessToken);

    }

    public Long findUserPkBySocialIdAndSocialProvider(String socialId, String socialProvider) {
        // 여기서 null 오던가 유저 pk가 넘어와야 함
        return userRepo.findBySocialIdAndSocialProvider(socialId, socialProvider)
                       .map(User::getId)
                       .orElse(-1L);
    }


    public Long save(String socialId, String providerName) {

        User userEntity = User.builder() //
                              .socialId(socialId)
                              .socialProvider(providerName)
                              .userRole(UserRole.USER)
                              .build();
        userEntity.updateUserHistory(new UserHistory());
        userEntity.updateUserPortfolio(new UserPortfolio());
        userEntity.updateUserPool(new UserPool());

        return userRepo.save(userEntity)
                       .getId();
    }
}
