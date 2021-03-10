package MakeUs.Moira.service.security;


import MakeUs.Moira.domain.security.token.TokenProvider;
import MakeUs.Moira.domain.security.token.TokenProviderFactory;
import MakeUs.Moira.domain.user.User;
import MakeUs.Moira.domain.user.UserRepo;
import MakeUs.Moira.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProviderFactory tokenProviderFactory;
    private final UserRepo userRepo;


    public int getUserSocialId(String providerName, String accessToken) {

        TokenProvider tokenProvider = tokenProviderFactory.getTokenProvider(providerName);
        return tokenProvider.getUserId(accessToken);

    }

    public Optional<User> searchUserBySocialId(int socailId, String socialProvider){
        // 여기서 null 오던가 유저 pk가 넘어와야 함
        Optional<User> user =  userRepo.findBySocialIdAndSocialProvider(socailId, socialProvider);
        return user;
    }


    public Long save(int socialId, String providerName) {
        return userRepo.save(User.builder() //
                .socialId(socialId)
                .socialProvider(providerName)
                .userRole(UserRole.USER)
                .build()).getId();
    }
}
