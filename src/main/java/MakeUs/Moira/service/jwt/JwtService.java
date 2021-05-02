package MakeUs.Moira.service.jwt;


import MakeUs.Moira.config.security.JwtTokenProvider;
import MakeUs.Moira.controller.jwt.dto.LoginRequestDto;
import MakeUs.Moira.controller.jwt.dto.LoginResponseDto;
import MakeUs.Moira.domain.user.*;
import MakeUs.Moira.domain.userHistory.UserHistory;
import MakeUs.Moira.domain.userPool.UserPool;
import MakeUs.Moira.domain.userPortfolio.UserPortfolio;
import MakeUs.Moira.util.token.TokenProvider;
import MakeUs.Moira.util.token.TokenProviderFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class JwtService {

    private final UserRepo             userRepo;
    private final JwtTokenProvider     jwtTokenProvider;
    private final TokenProviderFactory tokenProviderFactory;


    public String getUserSocialId(String providerName, String accessToken) {
        TokenProvider tokenProvider = tokenProviderFactory.getTokenProvider(providerName);
        return tokenProvider.getUserSocialId(accessToken);
    }

    @Transactional
    public LoginResponseDto getToken(LoginRequestDto loginRequestDto) {

        String socialProvider = loginRequestDto.getSocialProvider();
        String token = loginRequestDto.getAccessToken();

        String socialId = getUserSocialId(socialProvider, token);
        User userEntity = userRepo.findBySocialIdAndSocialProvider(socialId, socialProvider);

        if (userEntity == null) { // 아예 처음인 유저
            User newUserEntity = saveFirstVisitUser(socialId, socialProvider);
            return toLoginResponseDto(newUserEntity, true);
        }

        if (userEntity.getNickname() == null) { // 소셜 로그인은 완료했지만, 회원가입이 완료되지 않은 유저
            return toLoginResponseDto(userEntity, true);
        }
        // 회원가입을 완료한 유저
        return toLoginResponseDto(userEntity, false);
    }


    private User saveFirstVisitUser(String socialId, String providerName) {

        User userEntity = User.builder() //
                              .socialId(socialId)
                              .socialProvider(providerName)
                              .userRole(UserRole.USER)
                              .build();
        userEntity.updateUserHistory(new UserHistory());
        userEntity.updateUserPortfolio(new UserPortfolio());
        userEntity.updateUserPool(new UserPool());
        return userRepo.save(userEntity);
    }

    private String createJwtToken(User userEntity) {
        return jwtTokenProvider.createToken(userEntity.getId()
                                                      .toString(), UserRole.USER.name());
    }

    private LoginResponseDto toLoginResponseDto(User newUserEntity, boolean b) {
        return LoginResponseDto.builder()
                               .jwtToken(createJwtToken(newUserEntity))
                               .needSignup(b)
                               .build();
    }
}
