package MakeUs.Moira.service.security;


import MakeUs.Moira.controller.security.dto.SocialDto;
import MakeUs.Moira.domain.security.token.TokenProvider;
import MakeUs.Moira.domain.security.token.TokenProviderFactory;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProviderFactory tokenProviderFactory;
    private final UserRepo userRepo;


    public String getUserSocialId(String providerName, String accessToken) {

        TokenProvider tokenProvider = tokenProviderFactory.getTokenProvider(providerName);
        return tokenProvider.getUserSocialId(accessToken);

    }

    public SocialDto findBySocialIdAndSocialProvider(String socialId, String socialProvider) {
        // 여기서 null 오던가 유저 pk가 넘어와야 함
        Long userPk = userRepo.findBySocialIdAndSocialProvider(socialId, socialProvider)
                .map(User::getId)
                .orElse(-1L);
        SocialDto socialDto = new SocialDto();
        socialDto.setUserPk(userPk);
        return socialDto;
    }


    public Long save(String socialId, String providerName) {
        return userRepo.save(User.builder() //
                .socialId(socialId)
                .socialProvider(providerName)
                .userRole(UserRole.USER)
                .build()).getId();
    }
}
