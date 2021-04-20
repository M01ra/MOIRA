package MakeUs.Moira.util.token;

import MakeUs.Moira.exception.CustomException;
import MakeUs.Moira.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProviderFactory {

    private final List<TokenProvider> tokenProviderList;

    public TokenProvider getTokenProvider(String providerName) {
        return tokenProviderList.stream()
                .filter(tokenProvider -> tokenProvider.getProviderName().equals(providerName))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN_PROVIDER));
    }
}
