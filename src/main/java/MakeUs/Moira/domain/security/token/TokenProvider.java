package MakeUs.Moira.domain.security.token;

public interface TokenProvider {
    String getProviderName();
    String getUserSocialId(String accessToken);
}
