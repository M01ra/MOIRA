package MakeUs.Moira.domain.security.token;

public interface TokenProvider {
    String getProviderName();
    int getUserId(String accessToken);
}
